package net.mantucon.jsync.actions;

import net.mantucon.jsync.actions.steps.MkdirStep;

import java.io.File;

/**
 * Created by marcus on 15.04.14.
 */
public class RmDirAction implements Action  {

    private final File file;

    Step undoStep;

    public RmDirAction(File file) {
        this.file = file;
    }

    @Override
    public void perform() {
        if (file.exists() && !file.isDirectory()) {
            throw new ActionFailedException("File to delete is not a directory!");
        } else if (!file.canWrite()) {
            throw new ActionFailedException("Destination directory is write protected");
        }
        undoStep = new MkdirStep(file);
        file.delete();
        Thread.yield();
        if (file.exists()) {
            throw new ActionFailedException("RMDIR failed!");
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
