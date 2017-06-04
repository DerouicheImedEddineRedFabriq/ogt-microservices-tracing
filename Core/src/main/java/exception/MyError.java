package exception;

/**
 * The class that encapsulated the errors.
 *
 * @author m.mazigh
 */
public class MyError {
    private int code;
    private String message;

    /**
     * Empty constructor
     */
    public MyError() {
    }

    /**
     * Creates a new {@link MyError}.
     *
     * @param code    The code error.
     * @param message The message of error.
     */
    public MyError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return The error code.
     */
    public int getCode() {
        return code;
    }

    /**
     * @return The error message.
     */
    public String getMessage() {
        return message;
    }
}
