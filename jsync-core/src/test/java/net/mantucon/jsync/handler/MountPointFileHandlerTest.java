package net.mantucon.jsync.handler;

import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.Fixtures.CopyFileFixture;
import net.mantucon.jsync.Fixtures.FileFixture;
import net.mantucon.jsync.actions.Action;
import net.mantucon.jsync.actions.Step;
import net.mantucon.jsync.transaction.SynchronizationTransaction;
import net.mantucon.jsync.util.JSyncStandardLogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static net.mantucon.jsync.Fixtures.FileFixture.*;
import static net.mantucon.jsync.FileAssertions.*;
import static org.junit.Assert.*;

/**
 * Created by marcus on 05.05.14.
 */
public class MountPointFileHandlerTest {

    File sourceRoot;
    File destRoot;
    File remoteRoot;

    CopyFileFixture.FileStructure sourceFileStructure;
    CopyFileFixture.FileStructure destFileStructure;
    CopyFileFixture.FileStructure remoteFileStructure;

    Configuration configuration;

    @Before
    public void setUp() throws Exception {
        sourceRoot = createTempDirectory("source");

        sourceFileStructure = CopyFileFixture.initializeTestFileStructure(sourceRoot);
        sourceFileStructure.create();

        destRoot = createTempDirectory("dest");

        destFileStructure = CopyFileFixture.initializeTestFileStructure(destRoot);

        remoteRoot = createTempDirectory("remote");

        remoteFileStructure = CopyFileFixture.initializeTestFileStructure(remoteRoot);

        configuration = new Configuration(sourceRoot, destRoot, remoteRoot,new SynchronizationTransaction(),MountPointFileHandler.class.getName(),true,new JSyncStandardLogger());

    }

    @After
    public void tearDown() throws Exception {
        sourceFileStructure.destroy();
        destFileStructure.destroy();

    }

    /**
     * Tests the correct structural copy of file1 build to mirror
     * @throws Exception
     */
    @Test
    public void testHandleFile() throws Exception {
        Handler  handler = new HandlerFactory(configuration).produceHandlerInstance();
        assertNotNull(handler);

        Action action = handler.handleSourceFile(sourceFileStructure.file1);
        action.perform();

        assertFileExists(destFileStructure.dir1);
        assertFileDoesntExist(destFileStructure.dir11);
        assertFileDoesntExist(destFileStructure.dir12);
        assertFileDoesntExist(destFileStructure.dir121);
        assertFileExists(destFileStructure.file1);
        assertFileDoesntExist(destFileStructure.file121);
        assertIsDir(destFileStructure.dir1);
        assertContentIsEqual(sourceFileStructure.file1, destFileStructure.file1);

        Step undo = action.getUndoStep();
        undo.perform();

        assertFileDoesntExist(destFileStructure.dir1);
        assertFileDoesntExist(destFileStructure.dir11);
        assertFileDoesntExist(destFileStructure.dir12);
        assertFileDoesntExist(destFileStructure.dir121);
        assertFileDoesntExist(destFileStructure.file1);
        assertFileDoesntExist(destFileStructure.file121);

    }

    @Test
    public void testHandleFile2() throws Exception {
        Handler  handler = new HandlerFactory(configuration).produceHandlerInstance();
        assertNotNull(handler);

        Action action = handler.handleSourceFile(sourceFileStructure.file121);
        action.perform();

        assertFileExists(destFileStructure.dir1);
        assertFileDoesntExist(destFileStructure.dir11);
        assertFileExists(destFileStructure.dir12);
        assertFileExists(destFileStructure.dir121);
        assertFileDoesntExist(destFileStructure.file1);
        assertFileExists(destFileStructure.file121);
        assertIsDir(destFileStructure.dir1);
        assertIsDir(destFileStructure.dir12);
        assertIsDir(destFileStructure.dir121);
        assertContentIsEqual(sourceFileStructure.file121, destFileStructure.file121);

        Step undo = action.getUndoStep();
        undo.perform();

        assertFileDoesntExist(destFileStructure.dir1);
        assertFileDoesntExist(destFileStructure.dir11);
        assertFileDoesntExist(destFileStructure.dir12);
        assertFileDoesntExist(destFileStructure.dir121);
        assertFileDoesntExist(destFileStructure.file1);
        assertFileDoesntExist(destFileStructure.file121);

    }

    /**
     * Simulates the deletion of a specific file within the build tree.
     * This deletion candidate has to be identified by the handleTargetFile
     * method of the handler
     *
     * @throws Exception
     */
    @Test
    public void testHandleFile3() throws Exception {
        Handler  handler = new HandlerFactory(configuration).produceHandlerInstance();
        assertNotNull(handler);

        sourceFileStructure.file121.delete();
        destFileStructure.create();
        remoteFileStructure.create();

        assertFileExists(destFileStructure.dir1);
        assertFileExists(destFileStructure.dir11);
        assertFileExists(destFileStructure.dir12);
        assertFileExists(destFileStructure.dir121);
        assertFileExists(destFileStructure.file1);
        assertFileExists(destFileStructure.file121);
        assertIsDir(destFileStructure.dir1);
        assertIsDir(destFileStructure.dir12);
        assertIsDir(destFileStructure.dir121);

        Action action = handler.handleTargetFile(destFileStructure.file121);
        action.perform();

        assertFileExists(destFileStructure.dir1);
        assertFileExists(destFileStructure.dir11);
        assertFileExists(destFileStructure.dir12);
        assertFileExists(destFileStructure.dir121);
        assertFileExists(destFileStructure.file1);
        assertFileDoesntExist(destFileStructure.file121);
        assertIsDir(destFileStructure.dir1);
        assertIsDir(destFileStructure.dir12);
        assertIsDir(destFileStructure.dir121);

    }


    /**
     * Simulates the structural deletion of a complete subtree.
     *
     * @throws Exception
     */
    @Test
    public void testHandleFile4() throws Exception {
        Handler  handler = new HandlerFactory(configuration).produceHandlerInstance();
        assertNotNull(handler);

        sourceFileStructure.file121.delete();
        sourceFileStructure.dir121.delete();
        sourceFileStructure.dir12.delete();

        destFileStructure.create();
        remoteFileStructure.create();

        assertFileExists(destFileStructure.dir1);
        assertFileExists(destFileStructure.dir11);
        assertFileExists(destFileStructure.dir12);
        assertFileExists(destFileStructure.dir121);
        assertFileExists(destFileStructure.file1);
        assertFileExists(destFileStructure.file121);
        assertIsDir(destFileStructure.dir1);
        assertIsDir(destFileStructure.dir12);
        assertIsDir(destFileStructure.dir121);

        Action action = handler.handleTargetFile(destFileStructure.file121);
        action.perform();

        assertFileExists(destFileStructure.dir1);
        assertFileExists(destFileStructure.dir11);
        assertFileExists(destFileStructure.file1);
        assertFileDoesntExist(destFileStructure.file121);
        assertFileDoesntExist(destFileStructure.dir121);
        assertFileDoesntExist(destFileStructure.dir12);
        assertIsDir(destFileStructure.dir1);

    }

}
