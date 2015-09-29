package com.github.btnguyen2k.mpps.command;

/**
 * Represents memached's DELETE command.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since 0.1.0
 */
public class DeleteCommand extends AbstractWithKeyCommand {

    private final static String COMMAND = "delete";

    private boolean noreply = false;

    public DeleteCommand(String key) {
        super(COMMAND, key);
    }

    public DeleteCommand(String key, boolean noreply) {
        super(COMMAND, key);
        this.noreply = noreply;
    }

    public boolean isNoreply() {
        return noreply;
    }

    public DeleteCommand setNoreply(boolean noreply) {
        this.noreply = noreply;
        return this;
    }
}
