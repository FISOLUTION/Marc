package fis.marc.exception;

public class UserException extends RuntimeException {
    public UserException() {
    }

    public UserException(String message) {
        super(message);
    }
}
