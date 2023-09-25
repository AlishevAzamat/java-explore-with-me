package ru.practicum.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.server.dto.ApiError;
import ru.practicum.server.exception.ConflictException;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.exception.ValidationException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandlerController {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidatedException(final ValidationException e) {
        log.debug("Получен статус 400 Bad request {}", e.getMessage(), e);
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .errors(List.of(e.getStackTrace()))
                .message(e.getLocalizedMessage())
                .reason(e.getMessage())
                .status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundExceptionException(final NotFoundException e) {
        log.debug("Получен статус 404 Not Found {}", e.getMessage(), e);
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .errors(List.of(e.getStackTrace()))
                .message(e.getLocalizedMessage())
                .reason(e.getMessage())
                .status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidatedException(final MethodArgumentNotValidException e) {
        log.debug("Получен статус 400 Bad request {}", e.getMessage(), e);
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .errors(List.of(e.getStackTrace()))
                .message(e.getLocalizedMessage())
                .reason(e.getMessage())
                .status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .errors(List.of(e.getStackTrace()))
                .message(e.getLocalizedMessage())
                .reason(e.getMessage())
                .status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.debug("Получен статус 409 Conflict {}", e.getMessage(), e);
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .errors(List.of(e.getStackTrace()))
                .message(e.getLocalizedMessage())
                .reason(e.getMessage())
                .status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerConflictException(final ConflictException e) {
        log.debug("Получен статус 409 Conflict {}", e.getMessage(), e);
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .errors(List.of(e.getStackTrace()))
                .message(e.getLocalizedMessage())
                .reason(e.getMessage())
                .status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handlerException(final Throwable e) {
        log.debug("Получен статус 500 Internal Server Error {}", e.getMessage(), e);
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .errors(List.of(e.getStackTrace()))
                .message(e.getLocalizedMessage())
                .reason(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(final ConstraintViolationException e) {
        log.debug("Получен статус 400 Bad request {}", e.getMessage(), e);
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .errors(List.of(e.getStackTrace()))
                .message(e.getLocalizedMessage())
                .reason(e.getMessage())
                .status(HttpStatus.BAD_REQUEST).build();
    }
}