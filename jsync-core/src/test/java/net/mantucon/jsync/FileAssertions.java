package net.mantucon.jsync;

import junit.framework.AssertionFailedError;
import net.mantucon.jsync.actions.Action;

import java.io.File;
import static net.mantucon.jsync.util.FileUtil.*;

/**
 * Created by marcus on 29.04.14.
 */
public class FileAssertions {

    public static void assertFileExists(File file) {
        if (file == null || !file.exists()){
            throw new AssertionFailedError("File "+file+" does not exist!");
        }
    }

    public static void assertFileDoesntExist(File file) {
        if (file == null || file.exists()){
            throw new AssertionFailedError("File "+file+" exists but it should'nt!");
        }
    }

    public static void assertIsDir(File file) {
        if (file == null || !file.isDirectory()){
            throw new AssertionFailedError("File "+file+" is not a directory!");
        }
    }

    public static void assertIsFile(File file) {
        if (file == null || !file.isFile()){
            throw new AssertionFailedError("File "+file+" is not a directory!");
        }
    }

    public static void assertNotEmpty(File file) {
        if (file.length() <= 0){
            throw new AssertionFailedError("File is empty!");
        }
    }

    public static void assertContentIsEqual(File source, File dest) {
        String sourceMd5 = md5(source);
        String destMd5 = md5(dest);
        if (!sourceMd5.equals(destMd5)) {
            throw new AssertionFailedError("content does not match");
        }
    }

    public static void assertContentDiffers(File source, File dest) {
        String sourceMd5 = md5(source);
        String destMd5 = md5(dest);
        if (sourceMd5.equals(destMd5)) {
            throw new AssertionFailedError("content does not match");
        }
    }
}


