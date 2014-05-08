package net.mantucon.jsync.actions.steps;

import net.mantucon.jsync.actions.Action;
import net.mantucon.jsync.actions.RmDirAction;
import net.mantucon.jsync.actions.Step;

import java.io.File;

/**
 * Created by marcus on 15.04.14.
 */
public class MkdirStep implements Step {

    final File targetDir;

    public MkdirStep(File targetDir) {
        this.targetDir = targetDir;
    }

    @Override
    public void perform() {
        System.err.println("MKDIR "+targetDir.getAbsolutePath());
        targetDir.mkdir();
    }

    public File getTargetDir() {
        return targetDir;
    }
}
