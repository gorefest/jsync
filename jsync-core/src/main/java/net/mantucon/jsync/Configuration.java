package net.mantucon.jsync;

import net.mantucon.jsync.transaction.SynchronizationTransaction;

/**
 * Created by marcus on 14.04.14.
 */
public class Configuration {

    private static String _localBuildDir;
    private static String _localInstallDir;
    private static String _remoteSyncDir;
    private static SynchronizationTransaction synchronizationTransaction;
    private static String _handlerClassName = "net.mantucon.jsync.handler.MountPointFileHandler";

    public static void init(String localBuildDir, String localInstallDir, String remoteSyncDir) {
        _localBuildDir = localBuildDir;
        _localInstallDir = localInstallDir;
        _remoteSyncDir = remoteSyncDir;
    }

    public static final String getLocalBuildDir() {
        return _localBuildDir;
    }

    public static final String getLocalInstallDir() {
        return _localInstallDir;
    }

    public static final String getRemoteSyncDir() {
        return _remoteSyncDir;
    }

    public static final SynchronizationTransaction getSynchronizationTransaction() {
        return synchronizationTransaction;
    }

    public static String getHandlerClassName() {
        return _handlerClassName;
    }
}
