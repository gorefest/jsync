package net.mantucon.jsync.actions;

import java.io.File;

/**
 * Created by marcus on 05.05.14.
 */
public class SkipAction implements Action {

    private final File skipFile;

    public SkipAction(File skipFile) {
        this.skipFile = skipFile;
    }

    @Override
    public Step getUndoStep() {
        return null;
    }

    @Override
    public boolean isProcessed() {
        return false;
    }

    @Override
    public void perform() {
        System.out.println("Skipped file "+skipFile);
    }
}
