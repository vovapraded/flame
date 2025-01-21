package backend.academy.exception;

public class UnexpectedException extends AppException {
    public UnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedException(String message) {
        super(message);
    }
}
