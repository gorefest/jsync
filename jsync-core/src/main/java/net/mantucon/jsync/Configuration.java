package net.mantucon.jsync;

import net.mantucon.jsync.transaction.SynchronizationTransaction;
import net.mantucon.jsync.util.JSyncLogger;
import net.mantucon.jsync.util.JSyncStandardLogger;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by marcus on 14.04.14.
 */
public class Configuration {

    private static File  _localBuildDir;
    private static File  _localInstallDir;
    private static File _remoteSyncDir;
    private static SynchronizationTransaction synchronizationTransaction;
    private static String _handlerClassName = "net.mantucon.jsync.handler.MountPointFileHandler";
    public static boolean debugEnabled=false;
    public static JSyncLogger logger = new JSyncStandardLogger();

    private static final Set<String> alreadyDoneDirectories = Collections.synchronizedSet(new HashSet<String>());


    public static void init(String localBuildDir, String localInstallDir, String remoteSyncDir) {
        _localBuildDir = new File(localBuildDir);
        _localInstallDir = new File(localInstallDir);
        _remoteSyncDir = new File(remoteSyncDir);
        alreadyDoneDirectories.clear();
    }

    public static final File getLocalBuildDir() {
        return _localBuildDir;
    }

    public static final File getLocalInstallDir() {
        return _localInstallDir;
    }

    public static final File getRemoteSyncDir() {
        return _remoteSyncDir;
    }

    public static final SynchronizationTransaction getSynchronizationTransaction() {
        return synchronizationTransaction;
    }

    public static String getHandlerClassName() {
        return _handlerClassName;
    }

    public static boolean isDebugEnabled() {
        return debugEnabled;
    }
    
    public static JSyncLogger getLogger() {
        return logger;
    }

    public static void addDirectory(File directory) {
        alreadyDoneDirectories.add(directory.getAbsolutePath());
    }

    public static boolean isAlreadyDone(File directory) {
        return alreadyDoneDirectories.contains(directory.getAbsolutePath());
    }
}
