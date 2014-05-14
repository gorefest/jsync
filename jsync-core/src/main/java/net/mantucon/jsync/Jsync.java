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

    public void process(final Configuration configuration) {
        SynchronizationTransaction tx = new SynchronizationTransaction();

        if (!configuration.getLocalInstallDir().exists()) {
           configuration.getLogger().info("Local mirror directory does not exist. Going to create "+configuration.getLocalInstallDir().getAbsolutePath());
           configuration.getLocalInstallDir().mkdirs();
        }

        if (!configuration.getRemoteSyncDir().exists()) {
            configuration.getLogger().info("Remote directory does not exist. Going to create "+configuration.getRemoteSyncDir().getAbsolutePath());
            configuration.getRemoteSyncDir().mkdirs();
        }

        // 1. Find deletion candidates
        tx.addCleanupAction(crawlerPool.invoke(new CrawlerTask(configuration, configuration.getLocalInstallDir(), CrawlerTask.Mode.LOCAL_MIRROR)));

        // 2. Find override candidates
        tx.addAction(crawlerPool.invoke(new CrawlerTask(configuration, configuration.getLocalBuildDir(), CrawlerTask.Mode.BUILD_DIR)));

        final int numberOfSteps = configuration.getFilesToProcess();

        Thread thread = getWatcher(configuration, numberOfSteps);
        thread.start();

        try {
            tx.commit(configuration.getLogger());
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            configuration.setFilesToProcess(0);
        }
    }

    private Thread getWatcher(final Configuration configuration, final int numberOfSteps) {
        return new Thread(new Runnable() {
                @Override
                public void run() {
                    while (configuration.getFilesToProcess() > 0) {
                        try {
                            Thread.sleep(2000);
                            configuration.getLogger().info(configuration.getFilesToProcess() + " of " + numberOfSteps + " done");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    }
}
