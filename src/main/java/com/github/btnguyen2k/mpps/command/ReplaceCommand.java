package com.github.btnguyen2k.mpps.command;

/**
 * Represents memached's REPLACE command.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since 0.1.0
 */
public class ReplaceCommand extends AbstractStorageCommand {

    private final static String COMMAND = "replace";

    public ReplaceCommand(String command, String key, int flags, long expiryTime, int size) {
        super(COMMAND, key, flags, expiryTime, size);
    }

    public ReplaceCommand(String command, String key, int flags, long expiryTime, int size,
            byte[] data) {
        super(COMMAND, key, flags, expiryTime, size, data);
    }

    public ReplaceCommand(String command, String key, int flags, long expiryTime, int size,
            boolean noreply) {
        super(COMMAND, key, flags, expiryTime, size, noreply);
    }

    public ReplaceCommand(String command, String key, int flags, long expiryTime, int size,
            byte[] data, boolean noreply) {
        super(COMMAND, key, flags, expiryTime, size, data, noreply);
    }

}
