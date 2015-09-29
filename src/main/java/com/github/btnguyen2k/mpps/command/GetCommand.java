package com.github.btnguyen2k.mpps.command;

import java.util.List;
import java.util.regex.Matcher;

/**
 * Represents memached's GET command.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since 0.1.0
 */
public class GetCommand extends AbstractWithMultipleKeysCommand {

    private final static String COMMAND = "get";

    /**
     * Returns {@code null} if command's supplied params are valid, otherwise an
     * error message is returned.
     * 
     * <p>
     * Note: this method simply returns {@code null} as no special rules for GET
     * command's parameters.
     * </p>
     * 
     * @param cmdMatcher
     *            returned from
     *            {@link MemcachedCommand#validateCommandSyntax(String, String)}
     * @return
     */
    public static String validateCommandParams(Matcher cmdMatcher, int maxKeySize, long maxValueSize) {
        return null;
    }

    public GetCommand(String[] keys) {
        super(COMMAND, keys);
    }

    public GetCommand(List<String> keys) {
        super(COMMAND, keys);
    }
}
