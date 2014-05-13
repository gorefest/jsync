package net.mantucon.jsync;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.mantucon.jsync.transaction.SynchronizationTransaction;
import net.mantucon.jsync.util.JSyncLogger;
import net.mantucon.jsync.util.JSyncStandardLogger;

import java.io.File;
import java.util.*;

/**
 * Created by marcus on 14.04.14.
 */
public class Configuration {

    private final File localBuildDir;
    private final File localInstallDir;
    private final File remoteSyncDir;
    private final SynchronizationTransaction synchronizationTransaction;
    private final String handlerClassName;
    private final Boolean debugEnabled;
    private final JSyncLogger logger;

    private final Set<String> alreadyDoneDirectories = Collections.synchronizedSet(new HashSet<String>());

    public Configuration(File localBuildDir, File localInstallDir, File remoteSyncDir, SynchronizationTransaction synchronizationTransaction, String handlerClassName, Boolean debugEnabled, JSyncLogger logger) {
        this.localBuildDir = localBuildDir;
        this.localInstallDir = localInstallDir;
        this.remoteSyncDir = remoteSyncDir;
        this.synchronizationTransaction = synchronizationTransaction;
        this.handlerClassName = handlerClassName;
        this.debugEnabled = debugEnabled;
        this.logger = logger;
    }

    public final File getLocalBuildDir() {
        return localBuildDir;
    }

    public final File getLocalInstallDir() {
        return localInstallDir;
    }

    public final File getRemoteSyncDir() {
        return remoteSyncDir;
    }

    public final SynchronizationTransaction getSynchronizationTransaction() {
        return synchronizationTransaction;
    }

    public final String getHandlerClassName() {
        return handlerClassName;
    }

    public final boolean isDebugEnabled() {
        return debugEnabled;
    }


    public JSyncLogger getLogger() {
        return logger;
    }

    public void addDirectory(File directory) {
        alreadyDoneDirectories.add(directory.getAbsolutePath());
    }

    public boolean isAlreadyDone(File directory) {
        return alreadyDoneDirectories.contains(directory.getAbsolutePath());
    }

}
