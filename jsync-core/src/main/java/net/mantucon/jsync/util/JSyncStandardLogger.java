package net.mantucon.jsync.util;

import net.mantucon.jsync.Configuration;

/**
 * Created by marcus on 09.05.14.
 */
public class JSyncStandardLogger implements JSyncLogger {

    @Override
    public boolean isDebugEnabled() {
        return Configuration.isDebugEnabled();
    }

    @Override
    public void info(String what) {
        System.out.println("INFO : "+what);
    }

    @Override
    public void warn(String what) {
        System.err.println("WARN : "+what);
    }

    @Override
    public void error(String what) {
        System.err.println("ERROR: "+what);
    }
}
