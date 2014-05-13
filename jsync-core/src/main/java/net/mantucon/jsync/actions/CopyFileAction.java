package net.mantucon.jsync.actions;

import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.actions.steps.*;

import java.io.File;
import java.util.Stack;

/**
 * Created by marcus on 15.04.14.
 */
public class CopyFileAction extends BaseStep implements Action{

    private final File sourceFile;
    private final File destFile;

    StepChain undoSteps = new StepChain(configuration);;

    public CopyFileAction(Configuration configuration,File sourceFile, File destFile) {
        super(configuration);
        this.sourceFile = sourceFile;
        this.destFile = destFile;
    }


    @Override
    public void perform() {
        // target file exists?
        if (destFile.exists()) {
            undoSteps.add(new DeleteFileStep(configuration, destFile));
            undoSteps.add(new BackupFileInSituStep(configuration, destFile));
        } else {
            File parent = destFile.getParentFile();

            if (parent.exists() && (!parent.canWrite() || !parent.canRead()))
            {
                throw new ActionFailedException("Unable to read/write directory "+parent.getAbsolutePath());
            }
            Stack<MkdirStep> mkdirs = new Stack<>();
            MkdirStep md;

            undoSteps.add(new DeleteFileStep(configuration, destFile));
            while (parent != null && !parent.exists()) {
                md = new MkdirStep(configuration, parent);
                mkdirs.push(md);
                undoSteps.add(new DeleteFileStep(configuration, md.getTargetDir()));
                parent = parent.getParentFile();
            }

            while (!mkdirs.empty()) {
                md= mkdirs.pop();
                md.perform();
            }
        }
        CopyFileStep copy = new CopyFileStep(configuration, sourceFile,destFile);
        copy.perform();
    }

    @Override
    public Step getUndoStep() {
        return undoSteps;
    }

    @Override
    public boolean isProcessed() {
        return !undoSteps.isEmpty();
    }

    @Override
    public String toString() {
        return "CopyFileAction{" +
                "sourceFile=" + sourceFile +
                ", destFile=" + destFile +
                ", undoSteps=" + undoSteps +
                '}';
    }
}
