package net.mantucon.jsync.actions;

import net.mantucon.jsync.Configuration;

/**
 * Created by marcus on 13.05.14.
 */
class ActionStep {

    protected final Configuration configuration;

    public ActionStep(Configuration configuration) {
        this.configuration = configuration;
        this.configuration.inc();
    }

    public void process() {
        configuration.dec();
    }

}
