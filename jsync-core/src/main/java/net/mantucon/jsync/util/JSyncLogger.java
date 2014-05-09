package net.mantucon.jsync.util;

/**
 * Created by marcus on 09.05.14.
 */
public interface JSyncLogger {

    boolean isDebugEnabled();
    void info(String what);
    void warn(String what);
    void error(String what);
}
