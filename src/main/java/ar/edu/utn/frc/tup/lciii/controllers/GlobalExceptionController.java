package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.ErrorApi;
import ar.edu.utn.frc.tup.lciii.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
@Hidden
@AllArgsConstructor
public class GlobalExceptionController {

    @ExceptionHandler(EntityNotFoundException.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    public ResponseEntity<ErrorApi> handleError(NotFoundException exception) {
        ErrorApi error = buildError(HttpStatus.NOT_FOUND, exception.getMessage(), exception.getCause());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content)
    })
    public ResponseEntity<ErrorApi> handleError(IllegalArgumentException exception) {
        ErrorApi error = buildError(HttpStatus.BAD_REQUEST, exception.getMessage(), exception.getCause());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description =
                    "Internal Server Error", content =
            @Content)
    })
    public ResponseEntity<ErrorApi> handleError(Exception exception) {
        ErrorApi error = buildError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception.getCause());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private ErrorApi buildError(HttpStatus status, String message, Throwable cause) {
        return ErrorApi.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message + (cause != null ? " | Cause: " + cause : ""))
                .build();
    }
}
