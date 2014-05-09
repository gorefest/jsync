package net.mantucon.jsync;

import net.mantucon.jsync.transaction.SynchronizationTransaction;
import net.mantucon.jsync.util.JSyncLogger;
import net.mantucon.jsync.util.JSyncStandardLogger;

import java.io.File;

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

    public static void init(String localBuildDir, String localInstallDir, String remoteSyncDir) {
        _localBuildDir = new File(localBuildDir);
        _localInstallDir = new File(localInstallDir);
        _remoteSyncDir = new File(remoteSyncDir);
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
}
