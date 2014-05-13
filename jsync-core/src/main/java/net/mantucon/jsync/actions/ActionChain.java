package net.mantucon.jsync.actions;

import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.actions.steps.StepChain;
import net.mantucon.jsync.util.JSyncLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Created by marcus on 15.04.14.
 */
public class ActionChain extends RecursiveTask<Void> implements Action{

    ArrayList<Action> actions = new ArrayList<>();
    ArrayList<Action> processed = new ArrayList<>();

    private final Configuration configuration;

    public ActionChain(Configuration configuration) {
        this.configuration = configuration;
        configuration.inc();
    }

    @Override
    protected Void compute() {
        JSyncLogger logger = configuration.getLogger();
        List<ForkJoinTask> tasks = new ArrayList<>();
        for (Action action : actions) {
            if (action instanceof ForkJoinTask) {
                if (configuration.isDebugEnabled()) {
                    logger.info(Thread.currentThread().getName()+": handle action chain");
                }
                tasks.add(((ForkJoinTask) action));
                ((ForkJoinTask) action).fork();
            } else {
                if (configuration.isDebugEnabled()) {
                    logger.info(Thread.currentThread().getName()+": handle standard element ");
                }
                action.perform();
                processed.add(action);
            }
        }

        for (ForkJoinTask task : tasks) {
            task.join();
        }

        return null;
    }

    public ActionChain(Configuration configuration, Action... actions) {
        this.configuration = configuration;
        this.actions.addAll(Arrays.asList(actions));
    }

    @Override
    public void perform() {
        configuration.dec();
        compute();
    }

    @Override
    public Step getUndoStep() {
        StepChain result = new StepChain(configuration);
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
    public String toString() {
        return "ActionChain{" +
                "actions=" + (actions != null ? actions.size() : "null") +
                ", processed=" + (processed != null ? processed.size() : "null") +
                '}';
    }
}
