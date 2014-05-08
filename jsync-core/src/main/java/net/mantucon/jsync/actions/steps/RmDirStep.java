package net.mantucon.jsync.actions.steps;

import net.mantucon.jsync.actions.Action;
import net.mantucon.jsync.actions.ActionChain;
import net.mantucon.jsync.actions.Step;

import java.io.File;

/**
 * Created by marcus on 15.04.14.
 */
public class RmDirStep implements Step {

    private final File file;

    public RmDirStep(File file) {
        this.file = file;
    }

    @Override
    public void perform() {
        file.delete();
    }
}
