package com.github.btnguyen2k.mpps.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;
import java.util.regex.Matcher;

import com.github.btnguyen2k.mpps.command.MemcachedCommand;
import com.github.btnguyen2k.mpps.utils.MppsConstants;

/**
 * Decoder for memcached's ascii protocol.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since 0.1.0
 */
public class MemcachedAsciiProtocolDecoder extends ByteToMessageDecoder {

    /** Maximum length of a command line we're willing to decode. */
    private final int maxCmdLineLength;

    /** Maximum length of a value we're willing to decode. */
    private final long maxValueSize;

    /** Whether or not to throw an exception as soon as we exceed maxLength. */
    private final boolean failFast;

    /** True if we're discarding input because we're already over maxLength. */
    private boolean discarding;
    private int discardedBytes;

    /**
     * Creates a new decoder.
     * 
     * @param maxCmdLineLength
     *            the maximum length of the decoded command line. A
     *            {@link TooLongFrameException} is thrown if the length of the
     *            command line exceeds this value.
     * @param maxValueSize
     *            the maximum size of the decoded value. A
     *            {@link TooLongFrameException} is thrown if the length of the
     *            command line exceeds this value.
     */
    public MemcachedAsciiProtocolDecoder(final int maxCmdLineLength, final long maxValueSize) {
        this(maxCmdLineLength, maxValueSize, false);
    }

    /**
     * Creates a new decoder.
     * 
     * @param maxCmdLineLength
     *            the maximum length of the decoded command line. A
     *            {@link TooLongFrameException} is thrown if the length of the
     *            command line exceeds this value.
     * @param maxValueSize
     *            the maximum size of the decoded value. A
     *            {@link TooLongFrameException} is thrown if the length of the
     *            command line exceeds this value.
     * @param failFast
     *            If {@code true}, a {@link TooLongFrameException} is thrown as
     *            soon as the decoder notices the length of the frame will
     *            exceed {@code maxFrameLength} regardless of whether the entire
     *            frame has been read. If {@code false}, a
     *            {@link TooLongFrameException} is thrown after the entire frame
     *            that exceeds {@code maxFrameLength} has been read.
     */
    public MemcachedAsciiProtocolDecoder(final int maxCmdLineLength, final long maxValueSize,
            final boolean failFast) {
        this.maxCmdLineLength = maxCmdLineLength;
        this.maxValueSize = maxValueSize;
        this.failFast = failFast;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        MemcachedCommand command = ctx.attr(MppsConstants.ATTR_COMMAND).get();
        if (command == null) {
            // waiting for new command from client
            ByteBuf cmdLine = decodeCmdLine(ctx, in);
            if (cmdLine != null) {
                String strCmdLine = cmdLine.toString(MppsConstants.UTF8).trim();
                String strCmd = MemcachedCommand.isValidCommand(strCmdLine);
                if (strCmd != null) {
                    Matcher cmdMatcher = MemcachedCommand.validateCommandSyntax(strCmd, strCmdLine);
                    if (cmdMatcher == null) {
                        // bad command syntax
                        ctx.write(Unpooled.copiedBuffer(
                                MppsConstants.RESPONSE_TXT_CLIENT_ERROR_BAD_COMMAND,
                                MppsConstants.UTF8));
                        ctx.flush();
                        return;
                    }

                    String errorMsg = MemcachedCommand.validateCommandParams(strCmd, cmdMatcher,
                            MppsConstants.MAX_KEY_SIZE, maxValueSize);
                    if (errorMsg != null) {
                        // invalid params
                        ctx.write(Unpooled.copiedBuffer(errorMsg, MppsConstants.UTF8));
                        ctx.flush();
                        return;
                    }

                    // syntax ok
                    out.add(cmdLine);
                    return;
                }

                // no command matches
                ctx.write(Unpooled.copiedBuffer(MppsConstants.RESPONSE_TXT_ERROR,
                        MppsConstants.UTF8));
                ctx.flush();
            }
        }
    }

    /**
     * Create a frame out of the {@link ByteBuf} and return it.
     * 
     * @param ctx
     *            the {@link ChannelHandlerContext} which this
     *            {@link ByteToMessageDecoder} belongs to
     * @param buffer
     *            the {@link ByteBuf} from which to read data
     * @return frame the {@link ByteBuf} which represent the frame or
     *         {@code null} if no frame could be created.
     */
    protected ByteBuf decodeCmdLine(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        final int eol = findEndOfLine(buffer);
        if (!discarding) {
            if (eol >= 0) {
                // found EOL
                final ByteBuf frame;
                final int length = eol - buffer.readerIndex();
                final int delimLength = 2; // \r\n
                if (length > maxCmdLineLength) {
                    buffer.readerIndex(eol + delimLength);
                    fail(ctx, length);
                    return null;
                }
                frame = buffer.readSlice(length);
                buffer.skipBytes(delimLength);
                return frame.retain();
            } else {
                final int length = buffer.readableBytes();
                if (length > maxCmdLineLength) {
                    discardedBytes = length;
                    buffer.readerIndex(buffer.writerIndex());
                    discarding = true;
                    if (failFast) {
                        fail(ctx, "over " + discardedBytes);
                    }
                }
                return null;
            }
        } else {
            if (eol >= 0) {
                final int length = discardedBytes + eol - buffer.readerIndex();
                final int delimLength = 2; // \r\n
                buffer.readerIndex(eol + delimLength);
                discardedBytes = 0;
                discarding = false;
                if (!failFast) {
                    fail(ctx, length);
                }
            } else {
                discardedBytes += buffer.readableBytes();
                buffer.readerIndex(buffer.writerIndex());
            }
            return null;
        }
    }

    /**
     * Returns the index in the buffer if the end of line (EOL) found. Returns
     * -1 if no EOL was found in the buffer.
     * 
     * <p>
     * Note: EOL is \r\n
     * </p>
     * 
     * @param buffer
     * @return
     */
    private static int findEndOfLine(final ByteBuf buffer) {
        final int n = buffer.writerIndex();
        for (int i = buffer.readerIndex(); i < n; i++) {
            final byte b = buffer.getByte(i);
            if (b == '\r' && i < n - 1 && buffer.getByte(i + 1) == '\n') {
                return i;
            }
        }
        return -1;
    }

    private void fail(final ChannelHandlerContext ctx, int length) {
        fail(ctx, String.valueOf(length));
    }

    private void fail(final ChannelHandlerContext ctx, String length) {
        ctx.fireExceptionCaught(new TooLongFrameException("frame length (" + length
                + ") exceeds the allowed maximum (" + maxCmdLineLength + ')'));
    }
}
