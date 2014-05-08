package net.mantucon.jsync.actions;

import net.mantucon.jsync.FileAssertions;
import net.mantucon.jsync.Fixtures.FileFixture;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static net.mantucon.jsync.FileAssertions.*;
import static net.mantucon.jsync.Fixtures.FileFixture.*;
import static org.junit.Assert.assertNotNull;

/**
 * Created by marcus on 30.04.14.
 */
public class DeleteFileActionTest {

    File f1, f2;
    File directory;

    @Before
    public void before() throws Exception {
        directory = createTempDirectory("testDir");
        f1 = createSmallTestFile(directory, "test.txt");
        f2 = createSmallTestFile(directory, "test2.txt");
    }

    /**
     * test a file deletion plus a step rollback
     *
     * @throws Exception
     */
    @Test
    public void testGetUndoStep() throws Exception {
        DeleteFileAction action = new DeleteFileAction(f1);
        assertFileExists(f1);
        assertIsFile(f1);

        action.perform();
        assertFileDoesntExist(f1);

        Step undoAction = action.getUndoStep();
        assertNotNull(undoAction);

        undoAction.perform();
        assertFileExists(f1);

        assertContentIsEqual(f1,f2);

    }
}
