package net.mantucon.jsync.actions;

import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.actions.steps.BaseStep;
import net.mantucon.jsync.actions.steps.MkdirStep;
import net.mantucon.jsync.actions.steps.RmDirStep;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by marcus on 15.04.14.
 */
public class MkdirAction extends BaseStep implements Action {


    final File targetDir;
    private Step undoStep;

    public MkdirAction(Configuration configuration,File targetDir) {
        super(configuration);
        this.targetDir = targetDir;
    }

    @Override
    public void perform() {
        if (!configuration.isAlreadyDone(targetDir)) {
            if (targetDir.exists() && !targetDir.isDirectory()) {
                throw new ActionFailedException("Target file exists, but is not a directory!");
            } else if (!targetDir.exists()) {
                new MkdirStep(configuration, targetDir).perform();
                configuration.addDirectory(targetDir);
                undoStep = new RmDirStep(configuration, targetDir);
                Thread.yield();
                if (!targetDir.exists()) {
                    throw new ActionFailedException("MKDIR failed!");
                }
            }
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

    @Override
    public String toString() {
        return "MkdirAction{" +
                "targetDir=" + targetDir +
                ", undoStep=" + undoStep +
                '}';
    }
}
