package net.mantucon.jsync.actions.steps;

import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.actions.Step;

import java.io.File;
import java.io.IOException;

/**
 * Created by marcus on 15.04.14.
 */
public class BackupFileInSituStep extends BaseStep implements Step {

    private final File file;
    private final File tempFile;

    public BackupFileInSituStep(Configuration configuration,File file) {
        super(configuration);
        this.file = file;
        try {
            tempFile = File.createTempFile (file.getName(),"bak");
            tempFile.deleteOnExit();
            createBackup();
        } catch (IOException e) {
            throw new StepFailedException(e);
        }
    }

    @Override
    public void perform() {
        CopyFileStep copyFileStep = new CopyFileStep(configuration, tempFile, file);
        copyFileStep.perform();
    }

    private void createBackup() {
        CopyFileStep copyFileStep = new CopyFileStep(configuration, file, tempFile);
        copyFileStep.perform();
    }

}
