package com.github.btnguyen2k.mpps.command;

import java.text.MessageFormat;
import java.util.regex.Matcher;

import com.github.btnguyen2k.mpps.utils.MppsConstants;

/**
 * Base class for memached's storage commands.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since 0.1.0
 */
public abstract class AbstractStorageCommand extends AbstractWithKeyCommand {

    /**
     * Returns {@code null} if command's supplied params are valid, otherwise an
     * error message is returned.
     * 
     * <p>
     * Note: this method validates parameters for commands ADD, APPEND, PREPEND,
     * REPLACE and SET
     * </p>
     * 
     * <p>
     * Format {@code <cmd> <key> <flags> <exptime> <size> [noreply]}. Where
     * <ul>
     * <li>{@code cmd} is either {@code add}, {@code append}, {@code prepend},
     * {@code replace} or {@code set}.</li>
     * <li>{@code key} is a non-space string.</li>
     * <li>{@code flags} is a 32-bit unsigned integer.</li>
     * <li>{@code exptime}: {@code 0} means never expire, non-zero is expiry
     * time ({@code <= 30*24*3600} means "offset from current time",
     * {@code > 30*24*3600} means Unix timestamp where the key will expire)</li>
     * <li>{@code size} number of bytes in the data block to follow, *not*
     * including the delimiting {@code \r\n}</li>
     * <li>{@code "noreply"} optional parameter instructs the server to not send
     * the reply</li>
     * </ul>
     * </p>
     * 
     * @param cmdMatcher
     *            returned from
     *            {@link MemcachedCommand#validateCommandSyntax(String, String)}
     * @param maxKeySize
     * @param maxValueSize
     * @return
     */
    public static String validateCommandParams(Matcher cmdMatcher, int maxKeySize, long maxValueSize) {
        try {
            String key = cmdMatcher.group(1);
            long flags = Long.parseLong(cmdMatcher.group(2));
            // long exptime = Long.parseLong(cmdMatcher.group(3));
            long size = Long.parseLong(cmdMatcher.group(4));

            if (key.length() > maxKeySize) {
                return MessageFormat.format(MppsConstants.RESPONSE_TXT_SERVER_ERROR,
                        "key too large for cache");
            }

            if (flags < 0 || flags > Integer.MAX_VALUE) {
                return MppsConstants.RESPONSE_TXT_CLIENT_ERROR_BAD_COMMAND;
            }

            if (size < 0) {
                return MppsConstants.RESPONSE_TXT_CLIENT_ERROR_BAD_COMMAND;
            }

            if (size > maxValueSize) {
                return MessageFormat.format(MppsConstants.RESPONSE_TXT_SERVER_ERROR,
                        "object too large for cache");
            }

            return null;
        } catch (Exception e) {
            return MppsConstants.RESPONSE_TXT_CLIENT_ERROR_BAD_COMMAND;
        }
    }

    private int flags = 0;
    private long expiryTime = 0;
    private int size = 0;
    private byte[] data;
    private boolean noreply = false;

    public AbstractStorageCommand(String command, String key, int flags, long expiryTime, int size) {
        super(command, key);
        this.flags = flags;
        this.expiryTime = expiryTime;
        this.size = size;
    }

    public AbstractStorageCommand(String command, String key, int flags, long expiryTime, int size,
            byte[] data) {
        super(command, key);
        this.flags = flags;
        this.expiryTime = expiryTime;
        this.size = size;
        this.data = data;
    }

    public AbstractStorageCommand(String command, String key, int flags, long expiryTime, int size,
            boolean noreply) {
        super(command, key);
        this.flags = flags;
        this.expiryTime = expiryTime;
        this.size = size;
        this.noreply = noreply;
    }

    public AbstractStorageCommand(String command, String key, int flags, long expiryTime, int size,
            byte[] data, boolean noreply) {
        super(command, key);
        this.flags = flags;
        this.expiryTime = expiryTime;
        this.size = size;
        this.data = data;
        this.noreply = noreply;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isNoreply() {
        return noreply;
    }

    public void setNoreply(boolean noreply) {
        this.noreply = noreply;
    }
}
