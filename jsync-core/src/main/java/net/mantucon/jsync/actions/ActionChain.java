package net.mantucon.jsync.actions;

import net.mantucon.jsync.actions.steps.StepChain;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by marcus on 15.04.14.
 */
public class ActionChain implements Action, Runnable {

    ArrayList<Action> actions = new ArrayList<>();
    ArrayList<Action> processed = new ArrayList<>();


    public ActionChain() {

    }

    public ActionChain(Action... actions) {
        this.actions.addAll(Arrays.asList(actions));
    }

    @Override
    public void perform() {
        for (Action action : actions) {
            action.perform();
            processed.add(action);
        }
    }

    @Override
    public Step getUndoStep() {
        StepChain result = new StepChain();
        for (Action action : processed) {
            result.add(action.getUndoStep());
        }
        return result;
    }

    @Override
    public boolean isProcessed() {
        return processed.size() > 0;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    @Override
    public void run() {
        perform();
    }
}
