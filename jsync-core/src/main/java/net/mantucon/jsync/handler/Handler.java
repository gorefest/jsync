package net.mantucon.jsync.handler;

import net.mantucon.jsync.actions.Action;

import java.io.File;
import java.util.List;

/**
 * Created by marcus on 30.04.14.
 */
public interface Handler {

    public List<File> listSubfiles(File file);
    public Action handleSourceFile(File file);
    public Action handleTargetFile(File file);
}
