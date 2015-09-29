package com.github.btnguyen2k.mpps.utils;

import io.netty.util.AttributeKey;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import com.github.btnguyen2k.mpps.command.AddCommand;
import com.github.btnguyen2k.mpps.command.AppendCommand;
import com.github.btnguyen2k.mpps.command.GetCommand;
import com.github.btnguyen2k.mpps.command.MemcachedCommand;
import com.github.btnguyen2k.mpps.command.PrependCommand;
import com.github.btnguyen2k.mpps.command.ReplaceCommand;
import com.github.btnguyen2k.mpps.command.SetCommand;

/**
 * Commonly used constants.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since 0.1.0
 */
public class MppsConstants {

    public final static Charset UTF8 = Charset.forName("UTF-8");

    /** max key's size in bytes */
    public final static int MAX_KEY_SIZE = 250;

    /** default max value's size in bytes */
    public final static long DEFAULT_MAX_VALUE_SIZE = 1024L * 1024L;

    public final static String RESPONSE_TXT_ERROR = "ERROR\r\n";
    public final static String RESPONSE_TXT_CLIENT_ERROR = "CLIENT_ERROR {0}\r\n";
    public final static String RESPONSE_TXT_SERVER_ERROR = "SERVER_ERROR {0}\r\n";

    public final static String RESPONSE_TXT_CLIENT_ERROR_BAD_COMMAND = "CLIENT_ERROR bad command line format\r\n";

    public final static Map<String, Pattern> COMMAND_PATTERNS = new TreeMap<String, Pattern>();
    static {
        COMMAND_PATTERNS.put("get", Pattern.compile("^get\\s+([^\\s]+)(\\s+[^\\s]+)*$"));
        COMMAND_PATTERNS.put("gets", Pattern.compile("^gets\\s+([^\\s]+)(\\s+[^\\s]+)*$"));
        COMMAND_PATTERNS
                .put("set",
                        Pattern.compile("^set\\s+([^\\s]+)\\s+(\\d+)\\s+(-?\\d+)\\s+(\\d+)(\\s+noreply)?$"));
        COMMAND_PATTERNS
                .put("add",
                        Pattern.compile("^add\\s+([^\\s]+)\\s+(\\d+)\\s+(-?\\d+)\\s+(\\d+)(\\s+noreply)?$"));
        COMMAND_PATTERNS.put("replace", Pattern
                .compile("^replace\\s+([^\\s]+)\\s+(\\d+)\\s+(-?\\d+)\\s+(\\d+)(\\s+noreply)?$"));
        COMMAND_PATTERNS.put("append", Pattern
                .compile("^append\\s+([^\\s]+)\\s+(\\d+)\\s+(-?\\d+)\\s+(\\d+)(\\s+noreply)?$"));
        COMMAND_PATTERNS.put("prepend", Pattern
                .compile("^prepend\\s+([^\\s]+)\\s+(\\d+)\\s+(-?\\d+)\\s+(\\d+)(\\s+noreply)?$"));
    }

    public final static Map<String, Class<? extends MemcachedCommand>> COMMAND_SPECS = new TreeMap<String, Class<? extends MemcachedCommand>>();
    static {
        COMMAND_SPECS.put("get", GetCommand.class);
        COMMAND_SPECS.put("gets", GetCommand.class);
        COMMAND_SPECS.put("set", SetCommand.class);
        COMMAND_SPECS.put("add", AddCommand.class);
        COMMAND_SPECS.put("replace", ReplaceCommand.class);
        COMMAND_SPECS.put("append", AppendCommand.class);
        COMMAND_SPECS.put("prepend", PrependCommand.class);
    }

    public final static AttributeKey<MemcachedCommand> ATTR_COMMAND = AttributeKey
            .valueOf("command");
}
