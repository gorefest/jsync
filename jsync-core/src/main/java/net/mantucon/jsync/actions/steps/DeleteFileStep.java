package net.mantucon.jsync.actions.steps;

import net.mantucon.jsync.actions.Step;

import java.io.File;

/**
 * Created by marcus on 15.04.14.
 */
public class DeleteFileStep implements Step {

    private final File targetFile;

    public DeleteFileStep(File targetFile) {
        this.targetFile = targetFile;
    }


    @Override
    public void perform() {
        targetFile.delete();
    }

    public File getTargetFile() {
        return targetFile;
    }
}
