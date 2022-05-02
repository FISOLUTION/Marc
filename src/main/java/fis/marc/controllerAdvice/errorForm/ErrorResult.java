package fis.marc.controllerAdvice.errorForm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResult {
    private Long code;
    private String message;
}
