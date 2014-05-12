package net.mantucon.jsync;

/*
 * JSYNC Jenkins builder
 *
 * This builder is configurable through jenkins (verbose yes/no) and
 * allows a per-job configuration for syncing drives.
 *
 * It currenty takes three job parameters.
 *
 * 1. build dir - the build output directory from where the sync data is loaded
 * 2. local mirror - the local mirror directory which is used as a staging area
 *                   this directory is the exact copy of the remote file
 * 3. remote directory - remote drive where jsync is going to synchronize the files
 *
 */

import net.mantucon.jsync.actions.Step;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

@Mojo(name = "sync", defaultPhase = LifecyclePhase.NONE)
public class JsyncMojo
        extends AbstractMojo {

    @Parameter
    private Properties properties;

    @Parameter(defaultValue = "${buildDirectory}")
    private String buildDirectory;

    @Parameter(defaultValue = "${localMirror}")
    private String localMirror;

    @Parameter(defaultValue = "${remoteDirectory}")
    private String remoteDirectory;

    public void execute()
            throws MojoExecutionException {

        getLog().info("Starting Synchronization");

        if (buildDirectory == null || buildDirectory.trim().isEmpty()) {
            throw new RuntimeException("No build directory defined. Please define a build directory in the plugin properties");
        }
        if (localMirror == null || localMirror.trim().isEmpty()) {
            throw new RuntimeException("No local mirror directory defined. Please define a build directory in the plugin properties");
        }
        if (remoteDirectory == null || remoteDirectory.trim().isEmpty()) {
            throw new RuntimeException("No remote directory defined. Please define a build directory in the plugin properties");
        }

        Configuration.init(buildDirectory, localMirror, remoteDirectory);

        Jsync jsync = new Jsync();

        jsync.process();

        getLog().info("Done.");


    }
}
