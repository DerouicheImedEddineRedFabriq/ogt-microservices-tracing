package exception;

/**
 * A custom exception.
 *
 * @author m.mazigh
 */
public class MyException extends Exception {
    /**
     * Creates a new {@code MyException}.
     */
    public MyException() {
    }

    /**
     * Creates a new {@link MyException}.
     *
     * @param message the message.
     */
    public MyException(String message) {
        super(message);
    }

    /**
     * Creates a new {@code MyException}.
     *
     * @param message the message.
     * @param cause   the envelopped exception.
     */
    public MyException(String message, Throwable cause) {
        super(message, cause);
    }
}
