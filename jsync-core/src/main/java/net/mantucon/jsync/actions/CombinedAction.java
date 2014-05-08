package net.mantucon.jsync.actions;

import net.mantucon.jsync.actions.steps.StepChain;

/**
 * Created by marcus on 05.05.14.
 */
public class CombinedAction implements Action {

    Action mirrorAction;
    Action remoteAction;
    boolean mirrorProcessed;
    boolean remoteProcessed;


    public CombinedAction(Action mirrorAction, Action remoteAction) {
        this.mirrorAction = mirrorAction;
        this.remoteAction = remoteAction;
    }

    @Override
    public Step getUndoStep() {
        StepChain result = new StepChain();
        if (mirrorProcessed ) {
            result.add(mirrorAction.getUndoStep());
        }

        if (remoteProcessed) {
            result.add(remoteAction.getUndoStep());
        }
        return result;
    }

    @Override
    public boolean isProcessed() {
        return mirrorProcessed || remoteProcessed;
    }

    @Override
    public void perform() {
        mirrorAction.perform();
        mirrorProcessed = true;

        remoteAction.perform();
        remoteProcessed = true;
    }

    public Action getMirrorAction() {
        return mirrorAction;
    }

    public Action getRemoteAction() {
        return remoteAction;
    }
}
