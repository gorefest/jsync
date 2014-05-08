package net.mantucon.jsync.actions;

/**
 * Created by marcus on 15.04.14.
 */
public interface Action extends Step {

    public static final class ActionFailedException extends RuntimeException {
        public ActionFailedException(String message) {
            super(message);
        }

        public ActionFailedException(String message, Throwable cause) {
            super(message, cause);
        }

        public ActionFailedException(Throwable cause) {
            super(cause);
        }
    }

    public Step getUndoStep();

    public boolean isProcessed();
}
