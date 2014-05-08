package net.mantucon.jsync.actions;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by marcus on 16.04.14.
 */
public class RmDirActionTest {

    File tmp;
    File probeDir;
    File realProbe;

    @Before
    public void setup() throws Exception{
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
     * Success test
     *
     * This test shows how successfully a writeable dir can be removed
     *
     * @throws Exception
     */
    @Test
    public void testPerform() throws Exception {
        probeDir.mkdir();
        RmDirAction probe = new RmDirAction(probeDir);
        probe.perform();
        assertFalse(probeDir.exists());
        probe.getUndoStep().perform();
        assertTrue(probeDir.exists());
    }

    /**
     * Try to invoke an mkdir on a file
     * @throws Exception
     */
    @Test(expected = Action.ActionFailedException.class)
    public void testPerformBadDir() throws Exception {
        assertTrue(probeDir.createNewFile());
        RmDirAction probe = new RmDirAction(probeDir);
        probe.perform();

    }

    /**
     * Try to invoke an mkdir on a read-only dir
     * @throws Exception
     */
    @Test(expected = Action.ActionFailedException.class)
    public void testPerformBadDirectory() throws Exception {
        assertTrue(probeDir.mkdir());
        probeDir.setWritable(false,false);
        assertFalse(probeDir.canWrite());

        RmDirAction probe = new RmDirAction(probeDir);
        probe.perform();

    }

}
