package net.mantucon.jsync.actions.steps;

import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.actions.Action;
import net.mantucon.jsync.actions.Step;

import java.util.ArrayList;

/**
 * Created by marcus on 15.04.14.
 */
public class StepChain extends BaseStep implements Step {

    ArrayList<Step> steps = new ArrayList<>();

    public StepChain(Configuration configuration) {
        super(configuration);
    }

    @Override
    public void perform() {
        for (Step step : steps) {
            step.perform();
        }
    }

    public void add(Step step) {
        steps.add(step);
    }

    public boolean isEmpty() {
        return steps.isEmpty();
    }
}
