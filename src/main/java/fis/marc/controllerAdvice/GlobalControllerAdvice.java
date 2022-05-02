package fis.marc.controllerAdvice;

import fis.marc.controllerAdvice.errorForm.ErrorResult;
import fis.marc.exception.DuplicateNicknameException;
import fis.marc.exception.NoAuthorityException;
import fis.marc.exception.NoExistParsableException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateNicknameException.class)
    public ErrorResult signUpError(DuplicateNicknameException nicknameException) {
        return new ErrorResult(401L, nicknameException.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoAuthorityException.class)
    public ErrorResult authorityError(NoAuthorityException authorityException) {
        return new ErrorResult(401L, authorityException.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoExistParsableException.class)
    public ErrorResult noParsableError(NoExistParsableException parsableException) {
        return new ErrorResult(401L, parsableException.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResult noSuchElementError(NoSuchElementException noSuchElementException) {
        return new ErrorResult(401L, noSuchElementException.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResult illegalStateError(IllegalStateException illegalStateException) {
        return new ErrorResult(401L, illegalStateException.getMessage());
    }

}
