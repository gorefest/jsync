package net.mantucon.jsync.handler;

import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.actions.*;
import net.mantucon.jsync.util.FileUtil;
import net.mantucon.jsync.util.JSyncLogger;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import static net.mantucon.jsync.util.FileUtil.*;


/**
 * Created by IntelliJ IDEA.
 */
public class MountPointFileHandler extends Handler {

    public MountPointFileHandler(Configuration configuration) {
        super(configuration);
    }

    public Action handleSourceFile(File targetFile) {
        Action result = null;

        // Step 1 : exists counterpart?
        File localMirrorFile = FileUtil.build2local(configuration, targetFile);
        File remoteFile = FileUtil.local2remote(configuration, localMirrorFile);

        if (!localMirrorFile.exists()) {
//            System.out.println(Thread.currentThread().getName() + " : COPY");

            CopyFileAction mirrorCopy = new CopyFileAction(configuration, targetFile, localMirrorFile);
            CopyFileAction remoteCopy = new CopyFileAction(configuration, targetFile, remoteFile);
            CombinedAction combinedAction = new CombinedAction(configuration, mirrorCopy, remoteCopy);

            result = combinedAction;
        } else {
            String localMd5 = md5(localMirrorFile);
            String buildMd5 = md5(targetFile);
            if (!localMd5.equals(buildMd5)) {
                CopyFileAction mirrorCopy = new CopyFileAction(configuration, targetFile, localMirrorFile);
                CopyFileAction remoteCopy = new CopyFileAction(configuration, targetFile, remoteFile);
                CombinedAction combinedAction = new CombinedAction(configuration, mirrorCopy, remoteCopy);

                result = combinedAction;
            } else {
                final JSyncLogger logger = configuration.getLogger();
                if (configuration.isDebugEnabled()) {
                    logger.info(Thread.currentThread().getName() + " : SKIP MD5");
                }
                result = new SkipAction(configuration, targetFile);
            }
        }
//        System.out.println(Thread.currentThread().getName() + " : DONE");

        return result;
    }

    @Override
    public Action handleTargetFile(File infile) {
        ActionChain result = new ActionChain(configuration);
        File localFile = infile;
        File buildDirFile =  local2build(configuration, localFile);
        File remoteFile = local2remote(configuration, localFile);

        while (!buildDirFile.exists() && localFile.getAbsolutePath().startsWith(configuration.getLocalInstallDir().getAbsolutePath())) {
            result.addAction(localFile.isDirectory() ? new RmDirAction(configuration, localFile) : new DeleteFileAction(configuration, localFile));
            result.addAction(remoteFile.isDirectory() ? new RmDirAction(configuration, remoteFile) : new DeleteFileAction(configuration, remoteFile));

            localFile = localFile.getParentFile();
            buildDirFile =  local2build(configuration, local2build(configuration, localFile));
            remoteFile = local2remote(configuration, local2remote(configuration, localFile));

            if (!localFile.getAbsolutePath().startsWith(configuration.getLocalInstallDir().getAbsolutePath())) {
                throw new RuntimeException("Something pretty bad has happened, the localfile has no local mirror path prefix any more. To avoid any damage, I will bail out! Localfile is "+localFile.getAbsolutePath()+", local mirror path is "+configuration.getLocalInstallDir().getAbsolutePath());
            }
        }
        return result;
    }

    @Override
    public List<File> listSubfiles(File file) {
        return Arrays.asList(file.listFiles());
    }



}
