package net.mantucon.jsync.Fixtures;

import java.io.File;
import java.io.IOException;

/**
 * Created by marcus on 23.04.14.
 */
public class CopyFileFixture {

    /*
    1/11/
     file1.txt
     /12/121/file121.txt
    */


    public static class FileStructure {
        public File dir1;
        public File dir11;
        public File dir12;
        public File dir121;

        public File file1;
        public File file121;

        public void create() throws IOException {
            dir1.mkdir();
            dir11.mkdir();
            dir12.mkdir();
            dir121.mkdir();
            FileFixture.createLargeTestFile(file1);
            FileFixture.createLargeTestFile(file121);

        }

        public void destroy() {
            file121.delete();
            file1.delete();
            dir121.delete();
            dir12.delete();
            dir11.delete();
            dir1.delete();
        }

    }

    public static final FileStructure initializeTestFileStructure(File relativeOf) {
        FileStructure result = new FileStructure();
        result.dir1 = new File(relativeOf.getAbsolutePath()+File.separatorChar+"dir1");

        result.dir11 = new File(result.dir1.getAbsolutePath()+File.separatorChar+"dir11");

        result.dir12 = new File(result.dir1.getAbsolutePath()+File.separatorChar+"dir12");

        result.dir121 = new File(result.dir12.getAbsolutePath()+File.separatorChar+"dir121");

        result.file1 = new File(result.dir1.getAbsolutePath()+File.separatorChar+"file1.txt");

        result.file121 = new File(result.dir121.getAbsolutePath()+File.separatorChar+"file1.txt");

        return result;
    }


}
