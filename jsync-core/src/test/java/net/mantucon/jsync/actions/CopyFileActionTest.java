package net.mantucon.jsync.actions;

import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.Fixtures.FileFixture;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static net.mantucon.jsync.Fixtures.FileFixture.*;
import static net.mantucon.jsync.FileAssertions.*;
import static org.junit.Assert.assertNotNull;

/**
 * Tests known success scenarios.
 *
 * 1. target file does not exist, but the directory     (NEW situation)
 * 2. target file does exist                            (OVERRIDE situation)
 * 3. target file does not exist, directory does not exist (BRANDNEW situation)
 * 4. target file does not exist, multiple levels of parent directories dont exist either (MULTILEVEL situation)
 *
 * Created by marcus on 29.04.14.
 */
public class CopyFileActionTest {

    File sourceDir, sourceSubdir1, sourceSubdir2;
    File destDir, destSubdir1, destSubdir2;

    File s1,d1;
    File s2,d2;
    File s3,d3;
    File s4,d4;

    Configuration testConfig;


    @Before
    public void setup() throws IOException {
        testConfig = FileFixture.getTestConfiguration();

        sourceDir= createTempDirectory("source");
        s1 = createLargeTestFile(sourceDir, "test1.txt");
        destDir = createTempDirectory("dest");
        d1 = createEmptyFile(destDir,"test1.txt");

        s2 = createLargeTestFile(sourceDir,"test2.txt");
        d2 = createSmallTestFile(destDir, "test2.txt");

        sourceSubdir1 = createSubdirectory(sourceDir, "subdir");
        s3 = createSmallTestFile(sourceDir, "test3.txt");
        destSubdir1= createEmptyFile(destDir, "subdir");
        d3 = createEmptyFile(destSubdir1, "test3.txt");


        sourceSubdir2 = createSubdirectory(sourceSubdir1, "subdir2");
        s4 = createSmallTestFile( sourceSubdir2,"test4.txt");

        destSubdir2 = createEmptyFile(destSubdir1,"subdir2");
        d4 =  createEmptyFile(destSubdir2,"test4.txt");
    }

    /**
     *
     * Scenario : destination file does not exist
     *
     * @throws Exception
     */
    @Test
    public void testPerform1() throws Exception {
        CopyFileAction copyFileAction = new CopyFileAction(testConfig, s1,d1);
        copyFileAction.perform();

        assertFileExists(d1);
        assertNotEmpty(d1);
        assertContentIsEqual(s1,d1);

        Step undoAction = copyFileAction.getUndoStep();
        assertNotNull(undoAction);
        undoAction.perform();

        assertFileDoesntExist(d1);

    }

    /**
     *
     * Scenario : destination file does exist
     *
     * @throws Exception
     */
    @Test
    public void testPerform2() throws Exception {
        CopyFileAction copyFileAction = new CopyFileAction(testConfig, s2,d2);

        assertFileExists(d2);
        assertNotEmpty(d2);
        assertContentDiffers(s2,d2);

        copyFileAction.perform();

        assertFileExists(d2);
        assertNotEmpty(d2);
        assertContentIsEqual(s2,d2);

        Step undoAction = copyFileAction.getUndoStep();
        assertNotNull(undoAction);
        undoAction.perform();

        assertFileExists(d2);
        assertNotEmpty(d2);
        assertContentDiffers(s2,d2);

    }

    /**
     * Scenario 3: Destination file plus parent directory don't exist
     * @throws Exception
     */
    @Test
    public void testPerform3() throws Exception {
        CopyFileAction copyFileAction = new CopyFileAction(testConfig, s3,d3);
        assertFileDoesntExist(d3);
        assertFileDoesntExist(d3.getParentFile());

        copyFileAction.perform();
        assertFileExists(d3);
        assertContentIsEqual(s3,d3);

        Step undoAction = copyFileAction.getUndoStep();
        undoAction.perform();
        assertNotNull(undoAction);

        assertFileDoesntExist(d3);
        assertFileDoesntExist(d3.getParentFile());
    }


    /**
     * Scenario 4 : Multilevel parent directories do not exist
     *
     * @throws Exception
     */
    @Test
    public void testPerform4() throws Exception {
        CopyFileAction copyFileAction = new CopyFileAction(testConfig, s4,d4);
        assertFileDoesntExist(d4);
        assertFileDoesntExist(d4.getParentFile());
        assertFileDoesntExist(d4.getParentFile().getParentFile());

        copyFileAction.perform();
        assertFileExists(d4);
        assertFileExists(d4.getParentFile());
        assertIsDir(d4.getParentFile());
        assertIsDir(d4.getParentFile().getParentFile());
        assertIsFile(d4);
        assertContentIsEqual(s4, d4);

        Step undoAction = copyFileAction.getUndoStep();
        undoAction.perform();

        assertFileDoesntExist(d4);
        assertFileDoesntExist(d4.getParentFile());
        assertFileDoesntExist(d4.getParentFile().getParentFile());

    }


}
