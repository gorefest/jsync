package net.mantucon.jsync.actions;

import net.mantucon.jsync.actions.steps.MkdirStep;
import net.mantucon.jsync.actions.steps.RmDirStep;

import java.io.File;

/**
 * Created by marcus on 15.04.14.
 */
public class MkdirAction implements Action {

    final File targetDir;
    private Step undoStep;

    public MkdirAction(File targetDir) {
        this.targetDir = targetDir;
    }

    @Override
    public void perform() {
        if (targetDir.exists() && !targetDir.isDirectory()) {
            throw new ActionFailedException("Target file exists, but is not a directory!");
        }
        new MkdirStep(targetDir).perform();
        undoStep = new RmDirStep(targetDir);
        Thread.yield();
        if (!targetDir.exists()) {
            throw new ActionFailedException("MKDIR failed!");
        }
    }

    @Override
    public Step getUndoStep() {
        return undoStep;
    }

    @Override
    public boolean isProcessed() {
        return undoStep != null;
    }


}
