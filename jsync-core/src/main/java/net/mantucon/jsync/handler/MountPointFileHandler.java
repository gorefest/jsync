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
public class MountPointFileHandler implements Handler {

    MountPointFileHandler() {
        super();
    }


    public Action handleSourceFile(File targetFile) {
        Action result = null;

        // Step 1 : exists counterpart?
        File localMirrorFile = FileUtil.build2local(targetFile);
        File remoteFile = FileUtil.local2remote(localMirrorFile);

        if (!localMirrorFile.exists()) {
//            System.out.println(Thread.currentThread().getName() + " : COPY");

            CopyFileAction mirrorCopy = new CopyFileAction(targetFile, localMirrorFile);
            CopyFileAction remoteCopy = new CopyFileAction(targetFile, remoteFile);
            CombinedAction combinedAction = new CombinedAction(mirrorCopy, remoteCopy);

            result = combinedAction;
        } else {
            String localMd5 = md5(localMirrorFile);
            String buildMd5 = md5(targetFile);
            if (!localMd5.equals(buildMd5)) {
                CopyFileAction mirrorCopy = new CopyFileAction(targetFile, localMirrorFile);
                CopyFileAction remoteCopy = new CopyFileAction(targetFile, remoteFile);
                CombinedAction combinedAction = new CombinedAction(mirrorCopy, remoteCopy);

                result = combinedAction;
            } else {
                final JSyncLogger logger = Configuration.getLogger();
                if (logger.isDebugEnabled()) {
                    logger.info(Thread.currentThread().getName() + " : SKIP MD5");
                }
                result = new SkipAction(targetFile);
            }
        }
//        System.out.println(Thread.currentThread().getName() + " : DONE");

        return result;
    }

    @Override
    public Action handleTargetFile(File infile) {
        ActionChain result = new ActionChain();
        File localFile = infile;
        File buildDirFile =  local2build(localFile);
        File remoteFile = local2remote(localFile);

        while (!buildDirFile.exists()) {
            result.addAction(localFile.isDirectory() ? new RmDirAction(localFile) : new DeleteFileAction(localFile));
            result.addAction(remoteFile.isDirectory() ? new RmDirAction(remoteFile) : new DeleteFileAction(remoteFile));

            localFile = localFile.getParentFile();
            buildDirFile =  local2build(local2build(localFile));
            remoteFile = local2remote(local2remote(localFile));

            if (!localFile.getAbsolutePath().startsWith(Configuration.getLocalInstallDir())) {
                throw new RuntimeException("Something pretty bad has happened, the localfile has no local mirror path prefix any more. To avoid any damage, I will bail out!");
            }
        }
        return result;
    }

    @Override
    public List<File> listSubfiles(File file) {
        return Arrays.asList(file.listFiles());
    }



}
