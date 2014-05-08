package net.mantucon.jsync.actions.steps;

import net.mantucon.jsync.actions.Step;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by marcus on 23.04.14.
 */
public class StepChainTest {

    int count = 0;

    Step s1 = new Step() {
        @Override
        public void perform() {
            count++;
        }
    };

    Step s2 = new Step() {
        @Override
        public void perform() {
            count++;
        }
    };

    StepChain chain;

    @Before
    public void before() {
        chain = new StepChain();
    }


    @Test
    public void testPerform() throws Exception {
        chain.add(s1);
        chain.add(s2);
        chain.perform();
        assertEquals(2,count);
    }
}
