package fis.marc.exception;

public class NoExistParsableException extends RuntimeException {
    public NoExistParsableException() {
        super();
    }

    public NoExistParsableException(String message) {
        super(message);
    }
}
