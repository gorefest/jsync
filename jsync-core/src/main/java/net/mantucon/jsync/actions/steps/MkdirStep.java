package net.mantucon.jsync.actions.steps;

import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.actions.Action;
import net.mantucon.jsync.actions.RmDirAction;
import net.mantucon.jsync.actions.Step;
import net.mantucon.jsync.util.JSyncLogger;

import java.io.File;

/**
 * Created by marcus on 15.04.14.
 */
public class MkdirStep implements Step {

    final File targetDir;

    public MkdirStep(File targetDir) {
        this.targetDir = targetDir;
    }

    @Override
    public void perform() {
        JSyncLogger logger = Configuration.getLogger();
        if (!Configuration.isAlreadyDone(targetDir)) {
            if (logger.isDebugEnabled()) {
                logger.info(Thread.currentThread().getName()+" : MKDIR "+targetDir.getAbsolutePath());
            }
            targetDir.mkdir();
            Configuration.addDirectory(targetDir);
        }
    }

    public File getTargetDir() {
        return targetDir;
    }
}
