package net.mantucon.jsync.actions.steps;

import net.mantucon.jsync.actions.Action;
import net.mantucon.jsync.actions.CopyFileAction;
import net.mantucon.jsync.actions.Step;

import java.io.File;
import java.io.IOException;

/**
 * Created by marcus on 15.04.14.
 */
public class BackupFileStep implements Step {

    private final File file;
    private final File tempFile;

    public BackupFileStep(File file) {
        this.file = file;
        try {
            tempFile = File.createTempFile (file.getName(),"bak");
            tempFile.deleteOnExit();
        } catch (IOException e) {
            throw new StepFailedException(e);
        }
    }

    @Override
    public void perform() {
        CopyFileStep copyFileStep = new CopyFileStep(file, tempFile);
        copyFileStep.perform();
    }

    public File getFile() {
        return file;
    }

    public File getTempFile() {
        return tempFile;
    }
}
