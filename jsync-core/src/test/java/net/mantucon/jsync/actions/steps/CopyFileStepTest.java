package net.mantucon.jsync.actions.steps;

import net.mantucon.jsync.Fixtures.FileFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by marcus on 23.04.14.
 */
public class CopyFileStepTest {

    File infile;
    File outfile;

    @Before
    public void setup() throws Exception {
        infile = File.createTempFile("infile", "txt");
        outfile = File.createTempFile("outfile", "txt");
        FileFixture.createLargeTestFile(infile);
        infile.deleteOnExit();
        outfile.deleteOnExit();
    }

    @After
    public void after() throws Exception {
        infile.delete();
        outfile.delete();

    }

    @Test
    public void testPerform() throws Exception {
        CopyFileStep step = new CopyFileStep(infile, outfile);
        assertEquals(0L, outfile.length());
        step.perform();
        assertEquals(infile.length(), outfile.length());
        String content = FileFixture.getLargeString();

        byte[] buf = new byte[Long.valueOf(outfile.length()).intValue()];
        FileInputStream fis = new FileInputStream(outfile);
        fis.read(buf);
        String probe = new String(buf);

        assertEquals(content, probe);

    }
}
