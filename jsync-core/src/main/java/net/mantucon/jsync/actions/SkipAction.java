package net.mantucon.jsync.actions;

import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.util.JSyncLogger;

import java.io.File;

/**
 * Created by marcus on 05.05.14.
 */
public class SkipAction extends ActionStep implements Action {

    private final File skipFile;

    public SkipAction(Configuration configuration,File skipFile) {
        super(configuration);
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
        super.process();

        JSyncLogger logger = configuration.getLogger();
        if (configuration.isDebugEnabled()) {
            logger.info(Thread.currentThread().getName()+" : Skipped file "+skipFile);
        }
    }

    @Override
    public String toString() {
        return "SkipAction{" +
                "skipFile=" + skipFile +
                '}';
    }
}
