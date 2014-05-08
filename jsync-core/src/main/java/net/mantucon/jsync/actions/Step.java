package net.mantucon.jsync.actions;

/**
 * Created by marcus on 16.04.14.
 */
public interface Step {


    public static final class StepFailedException extends RuntimeException {
        public StepFailedException(String message) {
            super(message);
        }

        public StepFailedException(String message, Throwable cause) {
            super(message, cause);
        }

        public StepFailedException(Throwable cause) {
            super(cause);
        }
    }


    void perform();


}
