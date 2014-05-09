package net.mantucon.jsync.actions;

import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.util.JSyncLogger;

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
        JSyncLogger logger = Configuration.getLogger();
        if (logger.isDebugEnabled()) {
            logger.info(Thread.currentThread().getName()+" : Skipped file "+skipFile);
        }
    }
}
