package net.mantucon.jsync.actions;


import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.actions.steps.BackupFileInSituStep;
import net.mantucon.jsync.actions.steps.BackupFileStep;

import java.io.File;

/**
 * Created by marcus on 15.04.14.
 */
public class DeleteFileAction implements Action {

    private final File targetFile;

    Step undoStep;

    public DeleteFileAction(File targetFile) {
        this.targetFile = targetFile;
    }


    @Override
    public void perform() {
        if (targetFile.exists() && targetFile.isDirectory()) {
            throw new ActionFailedException(targetFile + " is a directory!");
        } else if (targetFile.exists() && !targetFile.canWrite()) {
            throw new ActionFailedException(targetFile +" is write protected!");
        }
        undoStep= new BackupFileInSituStep(targetFile);
        if (Configuration.isDebugEnabled()) {
            Configuration.getLogger().info("DELETE "+targetFile.getAbsolutePath());
        }
        targetFile.delete();
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
