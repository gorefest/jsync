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

    private static ThreadLocal<File> _localBuildDir = new ThreadLocal<>();
    private static ThreadLocal<File>_localInstallDir= new ThreadLocal<>();
    private static ThreadLocal<File>_remoteSyncDir= new ThreadLocal<>();
    private static ThreadLocal<SynchronizationTransaction> synchronizationTransaction= new ThreadLocal<>();
    private static ThreadLocal<String> _handlerClassName = new ThreadLocal<>();
    public static boolean debugEnabled=false;
    public static JSyncLogger logger = new JSyncStandardLogger();

    private static final ThreadLocal<Set<String>> alreadyDoneDirectories = new ThreadLocal<>();

    public static void init() {
        alreadyDoneDirectories.set(Collections.synchronizedSet(new HashSet<String>()));
    }

    public static void init(String localBuildDir, String localInstallDir, String remoteSyncDir) {
        _localBuildDir.set(new File(localBuildDir));
        _localInstallDir.set(new File(localInstallDir));
        _remoteSyncDir.set(new File(remoteSyncDir));
        _handlerClassName.set("net.mantucon.jsync.handler.MountPointFileHandler");
        init();
    }

    public static final File getLocalBuildDir() {
        return _localBuildDir.get();
    }

    public static final File getLocalInstallDir() {
        return _localInstallDir.get();
    }

    public static final File getRemoteSyncDir() {
        return _remoteSyncDir.get();
    }

    public static final SynchronizationTransaction getSynchronizationTransaction() {
        return synchronizationTransaction.get();
    }

    public static String getHandlerClassName() {
        return _handlerClassName.get();
    }

    public static boolean isDebugEnabled() {
        return debugEnabled;
    }
    
    public static JSyncLogger getLogger() {
        return logger;
    }

    public static void addDirectory(File directory) {
        alreadyDoneDirectories.get().add(directory.getAbsolutePath());
    }

    public static boolean isAlreadyDone(File directory) {
        return alreadyDoneDirectories.get().contains(directory.getAbsolutePath());
    }
}
