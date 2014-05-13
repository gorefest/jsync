package net.mantucon.jsync.actions;

import junit.framework.TestCase;
import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.Fixtures.FileFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by marcus on 16.04.14.
 *
 * Testcases for mkdirAction
 *
 */
public class MkdirActionTest{

    File tmp;
    File probeDir;
    File realProbe;

    Configuration testConfig;

    @Before
    public void setup() throws Exception{
        testConfig = FileFixture.getTestConfiguration();

        tmp = File.createTempFile("foo","bar");
        tmp.deleteOnExit();
        probeDir = new File(tmp.getAbsolutePath()+File.pathSeparator+(new Date()).getTime());
    }

    @After
    public void after() {
        probeDir.delete();
        if (realProbe != null) {
            realProbe.delete();
        }
    }

    /**
     * success path test. Directory will be created, undo operation will remove it
     *
     * @throws Exception
     *
     */
    @Test
    public void testPerform() throws Exception {
        MkdirAction probe = new MkdirAction(testConfig, probeDir);
        probe.perform();
        assertTrue(probeDir.exists());
        assertTrue(probeDir.isDirectory());
        assertTrue(probeDir.canWrite());
        Step undo = probe.getUndoStep();
        assertNotNull(undo);
        undo.perform();
        assertFalse(probeDir.exists());
    }

    /**
     * This test will fail creating a directory, because a file already was created +
     * prior
     */
    @Test(expected = Action.ActionFailedException.class)
    public void testPerformBadFile() throws IOException {
        assertTrue(probeDir.createNewFile());
        assertTrue(probeDir.exists());
        assertTrue(probeDir.isFile());
        assertFalse(probeDir.isDirectory());
        MkdirAction probe = new MkdirAction(testConfig, probeDir);
        probe.perform();
    }

    /**
     * This test covers the case, that the parent directory
     * is not writeable to mkdir
     *
     */
    @Test(expected = Action.ActionFailedException.class)
    public void testPerformBadTargetDir() {
        // at first, create a new directory
        MkdirAction probe = new MkdirAction(testConfig, probeDir);
        probe.perform();
        assertTrue(probeDir.exists());
        assertTrue(probeDir.isDirectory());
        assertTrue(probeDir.canWrite());
        // second, revoke write privilege
        probeDir.setWritable(false,false);
        assertFalse(probeDir.canWrite());

        // now mkdir must fail
        File realProbe = new File(probeDir.getAbsolutePath()+File.separatorChar+(new Date()).getTime());
        probe = new MkdirAction(testConfig, realProbe);
        probe.perform();


    }

}
