package com.cesarvillarreal.portfolio.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Catch-all so nothing reaches a visitor but a generic message.
 * <p>
 * {@code ApiController} previously had no exception handling at all, and the application relied
 * entirely on inherited Spring Boot defaults — safe today, but nothing pinned it that way
 * ({@code .claude/rules/security.md}).
 * <p>
 * HTML routes get the existing {@code error} view; {@code /api/**} gets a JSON body with no
 * exception type, message, or stack trace in it.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String GENERIC_MESSAGE = "An unexpected error occurred.";

    @ExceptionHandler(Exception.class)
    public Object handleUnexpectedException(Exception ex, HttpServletRequest request) throws Exception {
        // Spring's own exceptions — 404 for an unknown path, 405 for a bad method, and the rest —
        // already carry the correct status and leak nothing. Rethrowing the *same instance* makes
        // ExceptionHandlerExceptionResolver fall through to the default resolvers silently. Without
        // this, a catch-all on Exception would turn every 404 on the site into a 500.
        if (ex instanceof ErrorResponse) {
            throw ex;
        }

        // The detail stays server-side. This is the only place it is recorded.
        log.error("Unhandled exception for {} {}", request.getMethod(), request.getRequestURI(), ex);

        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            "message", GENERIC_MESSAGE));
        }

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return modelAndView;
    }

    private boolean isApiRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/");
    }
}
