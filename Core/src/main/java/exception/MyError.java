package exception;

/**
 *
 *
 * @author m.mazigh
 */
public class MyError {
    private int code;
    private String message;

    public MyError() {
    }

    public MyError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
