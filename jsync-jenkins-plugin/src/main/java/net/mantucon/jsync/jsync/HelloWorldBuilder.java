package net.mantucon.jsync.jsync;
import hudson.Launcher;
import hudson.Extension;
import hudson.util.FormValidation;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.Jsync;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link HelloWorldBuilder} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields
 * to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform(AbstractBuild, Launcher, BuildListener)}
 * method will be invoked. 
 *
 * @author Kohsuke Kawaguchi
 */
public class HelloWorldBuilder extends Builder {

    private final String buildDirectory;
    private final String localMirror;
    private final String remoteDirectory;

    @DataBoundConstructor
    public HelloWorldBuilder(String buildDirectory, String localMirror, String remoteDirectory) {
        this.buildDirectory = buildDirectory;
        this.localMirror = localMirror;
        this.remoteDirectory = remoteDirectory;
    }

    public String getBuildDirectory() {
        return buildDirectory;
    }

    public String getLocalMirror() {
        return localMirror;
    }

    public String getRemoteDirectory() {
        return remoteDirectory;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {

        Configuration.logger = new JenkinsLogger(listener);

        Configuration.logger.info("Begin work");
        Configuration.logger.info("Build output directory  : "+getBuildDirectory());
        Configuration.logger.info("Local mirror directory  : "+getLocalMirror());
        Configuration.logger.info("Remote deploy directory : "+getRemoteDirectory());
        Jsync jsync = new Jsync();
        Configuration.init(getBuildDirectory(), getLocalMirror(), getRemoteDirectory());
        Configuration.debugEnabled = getDescriptor().isEnabled();
        jsync.process();

        return true;
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    /**
     * Descriptor for {@link HelloWorldBuilder}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See <tt>src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        /**
         * To persist global configuration information,
         * simply store it in a field and call save().
         *
         * <p>
         * If you don't want fields to be persisted, use <tt>transient</tt>.
         */
        private boolean isEnabled;

        /**
         * In order to load the persisted global configuration, you have to 
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        /**
         * Performs on-the-fly validation of the form field 'name'.
         *
         * @param value
         *      This parameter receives the value that the user has typed.
         * @return
         *      Indicates the outcome of the validation. This is sent to the browser.
         *      <p>
         *      Note that returning {@link FormValidation#error(String)} does not
         *      prevent the form from being saved. It just means that a message
         *      will be displayed to the user. 
         */
        public FormValidation doCheckName(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a name");
            return FormValidation.ok();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types 
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "JSync Parameters";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            isEnabled = formData.getBoolean("isEnabled");
            save();
            return super.configure(req,formData);
        }

        public boolean isEnabled() {
            return isEnabled;
        }
    }
}

