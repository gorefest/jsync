package net.mantucon.jsync;

import net.mantucon.jsync.handler.MountPointFileHandler;
import net.mantucon.jsync.transaction.SynchronizationTransaction;
import net.mantucon.jsync.util.JSyncLogger;
import net.mantucon.jsync.util.JSyncStandardLogger;

import java.io.File;
import java.util.concurrent.ForkJoinPool;

/**
 * Hello world!
 *
 */
public class Jsync
{
    private final ForkJoinPool crawlerPool = new ForkJoinPool(8);

    public static void main( String[] args ) throws InterruptedException {

        SynchronizationTransaction tx = new SynchronizationTransaction();

        JSyncLogger logger = new JSyncStandardLogger();

        if (args == null || args.length != 3) {

            logger.error("Usage : <srcDir> <localMirrorDir> <remoteDir>");
            System.exit(-1);
        }

        logger.info("starting");

        Configuration configuration = new Configuration(new File(args[0]), new File(args[1]), new File(args[2]),tx, MountPointFileHandler.class.getName(), true, logger);

        logger.info("Build Dir : "+args[0]);
        logger.info("Local Mirror: "+args[1]);
        logger.info("Remote Dir : "+args[2]);

        new Jsync().process(configuration);

        logger.info("done");

    }

    public void process(Configuration configuration) {
        SynchronizationTransaction tx = new SynchronizationTransaction();

        // 1. Find deletion candidates
        tx.addCleanupAction(crawlerPool.invoke(new CrawlerTask(configuration, configuration.getLocalInstallDir(), CrawlerTask.Mode.LOCAL_MIRROR)));

        // 2. Find override candidates
        tx.addAction(crawlerPool.invoke(new CrawlerTask(configuration, configuration.getLocalBuildDir(), CrawlerTask.Mode.BUILD_DIR)));


        try {
            tx.commit(configuration.getLogger());
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
    }
}
