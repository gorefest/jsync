package net.mantucon.jsync.transaction;

import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.actions.Action;
import net.mantucon.jsync.actions.ActionChain;
import net.mantucon.jsync.util.JSyncLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Created by marcus on 30.04.14.
 */
public class SynchronizationTransaction {

    private List<Action> cleanupActions= new ArrayList<>();
    private List<Action> actions= new ArrayList<>();




    public void addCleanupAction(Action action) {
        cleanupActions.add(action);
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void commit() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        JSyncLogger logger = Configuration.getLogger();
        logger.info("starting Phase 1 :");
        int i = 0;

        for (Action action : cleanupActions) {
            if (action instanceof ActionChain) {
                executorService.submit((Runnable) action);
            } else {
                action.perform();
            }
        }
        try {
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        logger.info("...done. starting Phase 2 :");
        executorService = Executors.newFixedThreadPool(4);
        for (Action action : actions) {
            if (action instanceof ActionChain) {
                executorService.submit((Runnable) action);
            } else {
                action.perform();
            }
        }
        try {
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        logger.info("done.");

    }

    public void rollback() {
        for (Action action : actions) {
            action.getUndoStep().perform();
        }
    }



}
