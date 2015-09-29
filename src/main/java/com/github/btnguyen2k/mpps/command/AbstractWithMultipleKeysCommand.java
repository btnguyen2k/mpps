package com.github.btnguyen2k.mpps.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Base class for memcached command(s) that is associated with multiple keys.
 * 
 * @author Thanh Nguyen
 * @since 0.1.0
 */
public abstract class AbstractWithMultipleKeysCommand extends MemcachedCommand {
    private List<String> keys = new ArrayList<String>();

    protected AbstractWithMultipleKeysCommand(String name, List<String> keys) {
        super(name);
        if (keys != null) {
            this.keys.addAll(keys);
        }
    }

    protected AbstractWithMultipleKeysCommand(String name, String[] keys) {
        super(name);
        if (keys != null) {
            for (String key : keys) {
                this.keys.add(key);
            }
        }
    }

    public String[] getKeys() {
        return keys.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public AbstractWithMultipleKeysCommand setKey(List<String> keys) {
        this.keys.clear();
        if (keys == null) {
            this.keys.addAll(keys);
        }
        return this;
    }

    public AbstractWithMultipleKeysCommand setKey(String[] keys) {
        this.keys.clear();
        if (keys != null) {
            for (String key : keys) {
                this.keys.add(key);
            }
        }
        return this;
    }
}
