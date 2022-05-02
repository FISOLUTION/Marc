package fis.marc.exception;

public class NoAuthorityException extends RuntimeException {
    public NoAuthorityException() {
        super();
    }

    public NoAuthorityException(String message) {
        super(message);
    }
}
