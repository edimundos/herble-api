package eu.herble.herbleapi.errors;

import static java.util.stream.Collectors.joining;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity apiException(ApiException ex) {
        log.warn("{}\n{}\n{}", ex.getError(), ex.getMessage(), Arrays.stream(ex.getStackTrace()).limit(5).map(String::valueOf).collect(joining(System.lineSeparator())));

        return ResponseEntity.status(ex.getError().getStatus()).body(ex.getError());
    }
}
