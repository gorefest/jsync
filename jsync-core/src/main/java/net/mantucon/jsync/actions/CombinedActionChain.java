package net.mantucon.jsync.actions;

import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.actions.steps.BaseStep;
import net.mantucon.jsync.actions.steps.StepChain;

import java.util.ArrayList;

/**
 * Created by marcus on 15.04.14.
 */
public class CombinedActionChain extends BaseStep implements Action {

    ArrayList<Action> mirrorActions = new ArrayList<>();
    ArrayList<Action> remoteActions = new ArrayList<>();

    ArrayList<Action> processedActions = new ArrayList<>();

    public CombinedActionChain(Configuration configuration) {
        super(configuration);
    }


    @Override
    public void perform() {
        for (Action action : mirrorActions) {
            action.perform();
            processedActions.add(action);
        }
        for (Action action : remoteActions) {
            action.perform();
            processedActions.add(action);
        }
    }

    @Override
    public Step getUndoStep() {
        StepChain result = new StepChain(configuration);
        for (Action action : processedActions) {
            result.add(action.getUndoStep());
        }
        return result;
    }

    @Override
    public boolean isProcessed() {
        return processedActions.size() > 0;
    }

    public void addMirrorAction(Action action) {
        mirrorActions.add(action);
    }
    public void addRemoteAction(Action action) {
        remoteActions.add(action);
    }

    public ArrayList<Action> getMirrorActions() {
        return mirrorActions;
    }

    public ArrayList<Action> getRemoteActions() {
        return remoteActions;
    }

    @Override
    public String toString() {
        return "CombinedActionChain{" +
                "mirrorActions=" + mirrorActions +
                ", remoteActions=" + remoteActions +
                ", processedActions=" + processedActions +
                '}';
    }
}
