package net.mantucon.jsync.util;

import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.Fixtures.FileFixture;
import net.mantucon.jsync.handler.MountPointFileHandler;
import net.mantucon.jsync.transaction.SynchronizationTransaction;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static net.mantucon.jsync.FileAssertions.assertFileExists;
import static net.mantucon.jsync.util.FileUtil.build2local;
import static net.mantucon.jsync.util.FileUtil.local2remote;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by marcus on 05.05.14.
 */
public class FileUtilTest {

    File buildPath;
    File localPath;
    File remotePath;

    Configuration configuration;

    @Before
    public void setUp() throws Exception {
        buildPath = FileFixture.createTempDirectory("build");
        localPath = FileFixture.createTempDirectory("local");
        remotePath = FileFixture.createTempDirectory("remote");
        configuration = new Configuration(buildPath, localPath, remotePath, new SynchronizationTransaction(), MountPointFileHandler.class.getName(), true, new JSyncStandardLogger());

    }

    @Test
    public void testBuild2local() throws Exception {
        assertFalse(buildPath.getAbsolutePath().contains(localPath.getAbsolutePath()));
        File probe = FileFixture.createSmallTestFile(buildPath,"probe");

        assertFileExists(probe);
        File localFile = build2local(configuration, probe);

        assertEquals(probe.getName(), localFile.getName());
        assertTrue(localFile.getAbsolutePath().startsWith(localPath.getAbsolutePath()));

    }

    @Test
    public void testLocal2mirror() throws Exception {
        assertFalse(localPath.getAbsolutePath().contains(remotePath.getAbsolutePath()));
        File probe = FileFixture.createSmallTestFile(localPath,"probe");

        assertFileExists(probe);
        File remoteFile = local2remote(configuration, probe);

        assertEquals(probe.getName(), remoteFile.getName());
        assertTrue(remoteFile.getAbsolutePath().startsWith(remotePath.getAbsolutePath()));


    }
}
