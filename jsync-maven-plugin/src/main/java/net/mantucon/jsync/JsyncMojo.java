package net.mantucon.jsync;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
