package net.mantucon.jsync.actions.steps;

import net.mantucon.jsync.Fixtures.FileFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by marcus on 23.04.14.
 */
public class BackupFileStepTest {

    File probeFile;

    @Before
    public void setUp() throws Exception {
        probeFile = File.createTempFile("probe","txt");
        FileFixture.createSmallTestFile(probeFile);
        probeFile.deleteOnExit();
    }


    @After
    public void tearDown() throws Exception {
        probeFile.delete();
    }

    @Test
    public void testPerform() throws Exception {
        BackupFileStep candidate = new BackupFileStep(probeFile);

        assertTrue(candidate.getTempFile().isFile());
        assertTrue(candidate.getTempFile().exists());
        assertEquals(0L,candidate.getTempFile().length());

        candidate.perform();
        assertEquals(candidate.getFile().length(), candidate.getTempFile().length());

    }
}
