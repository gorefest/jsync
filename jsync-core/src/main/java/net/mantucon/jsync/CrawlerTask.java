package net.mantucon.jsync;

import net.mantucon.jsync.actions.Action;
import net.mantucon.jsync.actions.ActionChain;
import net.mantucon.jsync.handler.Handler;
import net.mantucon.jsync.handler.HandlerFactory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Created by IntelliJ IDEA.
 *
Root
 |
 |-Dir1
 |  |---- File11
 |  |---- File12
 |
 |-Dir2
 |  |-----Dir21
 |  |       |-----File21
 |  |       |-----File22

 ENTER: DIR (e.g. rel. root source, mirror dest)
            - if it exists, nothing to be done, otherwise create dir a) in mirror b) in destination
 SPAWN:  DIR1

 ENTER:  DIR1
 FILE :  FILE11
 FILE :  FILE12

 SPAWN:  DIR2
 ENTER:  DIR2

 SPAWN:  DIR21
 ENTER:  DIR21

 FILE :  FILE21
 FILE :  FILE22
 JOIN :  DIR21,DIR2
 JOIN :  DIR2,ROOT
 JOIN :  DIR1,ROOT
 *
 */
public class CrawlerTask extends RecursiveTask<Action> {

    private final File folder;
    private Mode mode;

    public static enum Mode { BUILD_DIR, LOCAL_MIRROR }

    CrawlerTask(File folder, Mode mode) {
        super();
        this.folder = folder;
        this.mode = mode;
    }

    @Override
    protected Action compute() {
        System.err.println(Thread.currentThread().getName() + " : entering " + folder.getName());
        ActionChain result = new ActionChain();


        // 1. ENTER
        // TODO : check using CombinedActionChain to differ provisioning to mirror or remote device!!
        Handler handler = HandlerFactory.produceHandlerInstance();


        List<RecursiveTask<Action>> taskList = new LinkedList<>();
        for (File subFile : handler.listSubfiles(folder)) {
            if (subFile.isDirectory()) {
                System.out.println(Thread.currentThread().getName() + " : Dir "+subFile.getName());
                // 2. SPAWN
                CrawlerTask task = new CrawlerTask(subFile, mode);
                taskList.add(task);
                task.fork();
            } else {
                // 3. FILE
                System.out.println(Thread.currentThread().getName() + " : Processing " + subFile.getName());

                switch (mode){

                    case BUILD_DIR:
                        result.addAction(handler.handleSourceFile(subFile));
                        break;
                    case LOCAL_MIRROR:
                        result.addAction(handler.handleTargetFile(subFile));
                        break;
                }
            }
        }

        for (RecursiveTask<Action> t : taskList) {
           result.addAction(t.join());
        }

        return result;
    }
}
