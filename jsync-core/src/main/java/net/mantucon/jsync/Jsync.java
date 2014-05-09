package net.mantucon.jsync;

import net.mantucon.jsync.transaction.SynchronizationTransaction;
import net.mantucon.jsync.util.JSyncLogger;

import java.io.File;
import java.util.concurrent.ForkJoinPool;

/**
 * Hello world!
 *
 */
public class Jsync
{
    private static final ForkJoinPool crawlerPool = new ForkJoinPool(8);

    public static void main( String[] args ) throws InterruptedException {
        JSyncLogger logger = Configuration.getLogger();

        if (args == null || args.length != 3) {

            logger.error("Usage : <srcDir> <localMirrorDir> <remoteDir>");
            System.exit(-1);
        }

        logger.info("starting");
        Configuration.init(args[0],args[1],args[2]);

        Configuration.debugEnabled = true;
        logger.info("Build Dir : "+args[0]);
        logger.info("Local Mirror: "+args[1]);
        logger.info("Remote Dir : "+args[2]);

        new Jsync().process();

        logger.info("done");

    }

    public void process() {
        SynchronizationTransaction tx = new SynchronizationTransaction();

        // 1. Find deletion candidates
        tx.addCleanupAction(crawlerPool.invoke(new CrawlerTask(Configuration.getLocalInstallDir(), CrawlerTask.Mode.LOCAL_MIRROR)));

        // 2. Find override candidates
        tx.addAction(crawlerPool.invoke(new CrawlerTask(Configuration.getLocalBuildDir(), CrawlerTask.Mode.BUILD_DIR)));


        try {
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
    }
}
