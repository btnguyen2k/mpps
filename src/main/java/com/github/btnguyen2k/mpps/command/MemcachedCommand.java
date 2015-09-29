package com.github.btnguyen2k.mpps.command;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.btnguyen2k.mpps.utils.MppsConstants;

/**
 * Base class representing a memcached command.
 * 
 * @author Thanh Nguyen
 * @since 0.1.0
 */
public abstract class MemcachedCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemcachedCommand.class);

    /**
     * Returns {@code null} if command's supplied params are valid, otherwise an
     * error message is returned.
     * 
     * @param strCmd
     * @param cmdMatcher
     *            returned from {@link #validateCommandSyntax(String, String)}
     * @param maxKeySize
     *            maximum key's size in bytes
     * @param maxValueSize
     *            maximum value's size in bytes
     * @return
     */
    public static String validateCommandParams(String strCmd, Matcher cmdMatcher, int maxKeySize,
            long maxValueSize) {
        Class<? extends MemcachedCommand> clazz = MppsConstants.COMMAND_SPECS.get(strCmd);
        if (clazz == null) {
            return MppsConstants.RESPONSE_TXT_ERROR;
        }
        try {
            Method method = clazz.getMethod("validateCommandParams", Matcher.class, int.class,
                    long.class);
            if (method != null) {
                return (String) method.invoke(null, cmdMatcher, maxKeySize, maxValueSize);
            }
            throw new RuntimeException("Class [" + clazz
                    + "] does not have static method [verifyCommandParams(Matcher, int, long)]!");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return MessageFormat.format(MppsConstants.RESPONSE_TXT_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Returns a {@link Matcher} instance if the supplied command line matches
     * the expected format, otherwise {@code null} is returned.
     * 
     * @param strCmd
     *            the "command" part of the command line
     * @param strCmdLine
     *            the entire command line
     * @return
     */
    public static Matcher validateCommandSyntax(String strCmd, String strCmdLine) {
        if (strCmd == null) {
            strCmd = isValidCommand(strCmdLine);
        }
        Pattern cmdPattern = strCmd != null ? MppsConstants.COMMAND_PATTERNS.get(strCmd) : null;
        Matcher m = cmdPattern != null ? cmdPattern.matcher(strCmdLine) : null;
        return m != null && m.matches() ? m : null;
    }

    /**
     * Returns the "command" part if a command line is valid (command is
     * recognized), otherwise {@code null} is returned.
     * 
     * @param strCmdLine
     * @return
     */
    public static String isValidCommand(String strCmdLine) {
        String[] tokens = strCmdLine.split("\\s+", 2);
        String strCommand = tokens != null ? tokens[0] : "";
        return MppsConstants.COMMAND_SPECS.get(strCommand) != null ? strCommand : null;
    }

    private String name;

    protected MemcachedCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MemcachedCommand setName(String name) {
        this.name = name;
        return this;
    }
}
