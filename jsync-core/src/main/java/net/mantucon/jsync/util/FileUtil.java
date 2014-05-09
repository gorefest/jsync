package net.mantucon.jsync.util;

import net.mantucon.jsync.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by marcus on 29.04.14.
 */
public class FileUtil {

    /**
     * @param localMirrorFile
     * @return the md5 hash for the file
     */
    public static final String md5(File localMirrorFile) {
        MessageDigest md = null;
        try (FileInputStream fileInputStream = new FileInputStream(localMirrorFile)){
            md = MessageDigest.getInstance("MD5");
            ByteBuffer buf = ByteBuffer.allocate(Long.valueOf(localMirrorFile.length()).intValue());
            fileInputStream.getChannel().read(buf);
            byte[] thedigest = md.digest(buf.array());
            return new String(thedigest);

        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * makes a build path file to a local mirror file
     *
     * @param f - the file on the build output directory
     * @return the local mirror path file
     */
    public static final File build2local(File f) {
        String realPath = f.getAbsolutePath();
        String localPath = Configuration.getLocalInstallDir().getAbsolutePath();
        realPath = realPath.replace(Configuration.getLocalBuildDir().getAbsolutePath(), localPath);
        return new File(realPath);
    }

    /**
     * makes a local path file to a build file
     *
     * @param f - the file on the build output directory
     * @return the local mirror path file
     */
    public static final File local2build(File f) {
        String realPath = f.getAbsolutePath();
        String localPath = Configuration.getLocalBuildDir().getAbsolutePath();
        realPath = realPath.replace(Configuration.getLocalInstallDir().getAbsolutePath(), localPath);
        return new File(realPath);
    }



    /**
     * makes a local mirror path file to a remote file
     * @param f - the local mirror path file
     * @return the remote file
     */
    public static final File local2remote(File f) {
        String realPath = f.getAbsolutePath();
        String remotePath = Configuration.getRemoteSyncDir().getAbsolutePath();
        realPath = realPath.replace(Configuration.getLocalInstallDir().getAbsolutePath(), remotePath);
        return new File(realPath);
    }

}
