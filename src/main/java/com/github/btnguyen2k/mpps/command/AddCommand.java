package com.github.btnguyen2k.mpps.command;

/**
 * Represents memached's ADD command.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since 0.1.0
 */
public class AddCommand extends AbstractStorageCommand {

    private final static String COMMAND = "add";

    public AddCommand(String command, String key, int flags, long expiryTime, int size) {
        super(COMMAND, key, flags, expiryTime, size);
    }

    public AddCommand(String command, String key, int flags, long expiryTime, int size, byte[] data) {
        super(COMMAND, key, flags, expiryTime, size, data);
    }

    public AddCommand(String command, String key, int flags, long expiryTime, int size,
            boolean noreply) {
        super(COMMAND, key, flags, expiryTime, size, noreply);
    }

    public AddCommand(String command, String key, int flags, long expiryTime, int size,
            byte[] data, boolean noreply) {
        super(COMMAND, key, flags, expiryTime, size, data, noreply);
    }

}
