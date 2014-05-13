package net.mantucon.jsync.jsync;

import hudson.model.TaskListener;
import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.util.JSyncLogger;

/**
 * Created by marcus on 09.05.14.
 */
public class JenkinsLogger implements JSyncLogger {

    private final TaskListener listener;

    public JenkinsLogger(TaskListener listener) {
        this.listener = listener;
    }


    @Override
    public void info(String s) {
        listener.getLogger().println(s);
    }

    @Override
    public void warn(String s) {
        listener.error(s);
    }

    @Override
    public void error(String s) {
        listener.fatalError(s);
    }
}
