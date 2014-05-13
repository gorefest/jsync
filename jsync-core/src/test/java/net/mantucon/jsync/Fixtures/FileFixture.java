package net.mantucon.jsync.Fixtures;

import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.handler.MountPointFileHandler;
import net.mantucon.jsync.transaction.SynchronizationTransaction;
import net.mantucon.jsync.util.JSyncStandardLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by marcus on 23.04.14.
 */
public class FileFixture {

    public static Configuration getTestConfiguration() throws IOException {
        File buildDir = createTempDirectory("build");
        File localMirror = createTempDirectory("local");
        File remoteDir = createTempDirectory("remote");
        return new Configuration(buildDir,localMirror,remoteDir,new SynchronizationTransaction()
                , MountPointFileHandler.class.getName(), true, new JSyncStandardLogger());
    }

    public static final void createSmallTestFile(File probeFile) throws IOException {
        String s = "This is a text";
        writefile(probeFile, s);
    }

    public static final File createSmallTestFile(File directory, String name) throws IOException {
        File probeFile = createEmptyFile(directory, name);
        writefile(probeFile, "THIS IS A TEXT");
        return probeFile;
    }


    public static final File createLargeTestFile(File directory, String name) throws IOException {
        File probeFile = createEmptyFile(directory, name);
        String sb = getLargeString();
        writefile(probeFile, sb);
        return probeFile;
    }

    public static final void createLargeTestFile(File probeFile) throws IOException {
        String sb = getLargeString();
        writefile(probeFile, sb);
    }

    public static final String getLargeString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4500; i++){
            sb.append("this is a text");
        }
        return sb.toString();
    }

    private static void writefile(File probeFile, String s) throws IOException {
        if (!probeFile.exists()) {
            probeFile.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(probeFile);
        fileOutputStream.write(s.getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }


    public static File createTempDirectory(String testName) throws IOException {
        File sourceRoot = File.createTempFile(testName, "dir");
        sourceRoot.delete();
        sourceRoot.mkdir();
        return sourceRoot;
    }

    public static File createSubdirectory(File file, String name) throws IOException {
        File result = createEmptyFile(file, name);
        result.mkdirs();
        return result;
    }

    public static File createEmptyFile(File directory, String name) {
        return new File(directory.getAbsolutePath()+File.separatorChar+name);
    }


}
