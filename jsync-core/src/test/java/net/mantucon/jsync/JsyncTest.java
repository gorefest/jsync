package net.mantucon.jsync;

import net.mantucon.jsync.Fixtures.CopyFileFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static net.mantucon.jsync.Fixtures.FileFixture.createTempDirectory;

/**
 * Created by marcus on 06.05.14.
 */
public class JsyncTest {

    File sourceRoot;
    File destRoot;
    File remoteRoot;

    CopyFileFixture.FileStructure sourceFileStructure;
    CopyFileFixture.FileStructure destFileStructure;
    CopyFileFixture.FileStructure remoteFileStructure;

    @Before
    public void setUp() throws Exception {
        sourceRoot = createTempDirectory("source");

        sourceFileStructure = CopyFileFixture.initializeTestFileStructure(sourceRoot);
        sourceFileStructure.create();
        sourceFileStructure.dir121.delete();

        destRoot = createTempDirectory("dest");
        destFileStructure = CopyFileFixture.initializeTestFileStructure(destRoot);
        destFileStructure.create();

        remoteRoot = createTempDirectory("remote");
        remoteFileStructure = CopyFileFixture.initializeTestFileStructure(remoteRoot);
        remoteFileStructure.create();

        Configuration.init(sourceRoot.getAbsolutePath(), destRoot.getAbsolutePath(), remoteRoot.getAbsolutePath());

    }


    @After
    public void tearDown() throws Exception {
        sourceFileStructure.destroy();
        destFileStructure.destroy();
        remoteFileStructure.destroy();
    }

    @Test
    public void testProcess() throws Exception {

    }
}
