package com.github.btnguyen2k.mpps.command;

/**
 * Represents memached's PREPEND command.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since 0.1.0
 */
public class PrependCommand extends AbstractStorageCommand {

    private final static String COMMAND = "prepend";

    public PrependCommand(String command, String key, int flags, long expiryTime, int size) {
        super(COMMAND, key, flags, expiryTime, size);
    }

    public PrependCommand(String command, String key, int flags, long expiryTime, int size,
            byte[] data) {
        super(COMMAND, key, flags, expiryTime, size, data);
    }

    public PrependCommand(String command, String key, int flags, long expiryTime, int size,
            boolean noreply) {
        super(COMMAND, key, flags, expiryTime, size, noreply);
    }

    public PrependCommand(String command, String key, int flags, long expiryTime, int size,
            byte[] data, boolean noreply) {
        super(COMMAND, key, flags, expiryTime, size, data, noreply);
    }

}
