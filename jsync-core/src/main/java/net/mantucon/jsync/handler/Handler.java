package net.mantucon.jsync.handler;

import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.actions.Action;

import java.io.File;
import java.util.List;

/**
 * Created by marcus on 30.04.14.
 */
public abstract class Handler {

    final Configuration configuration;

    protected Handler(Configuration configuration) {
        this.configuration = configuration;
    }

    public abstract List<File> listSubfiles(File file);
    public abstract Action handleSourceFile(File file);
    public abstract Action handleTargetFile(File file);
}
