package com.github.btnguyen2k.mpps.command;

/**
 * Base class for memcached command(s) that is associated with a key.
 * 
 * @author Thanh Nguyen
 * @since 0.1.0
 */
public abstract class AbstractWithKeyCommand extends MemcachedCommand {
    private String key;

    protected AbstractWithKeyCommand(String name, String key) {
        super(name);
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public AbstractWithKeyCommand setKey(String key) {
        this.key = key;
        return this;
    }
}
