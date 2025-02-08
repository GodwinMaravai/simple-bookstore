package be.kata.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * Maps internal exceptions thrown inside to {@link ProblemDetail} object.
 */
@ControllerAdvice
public class BookStoreResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(BookStoreResponseEntityExceptionHandler.class);
    private static final HttpHeaders EMPTY_HTTP_HEADERS = new HttpHeaders();

    private final ObjectMapper objectMapper;

    public BookStoreResponseEntityExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleBadRequestException(RuntimeException ex, WebRequest webRequest) throws JsonProcessingException {
        return handleException(ex, webRequest, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleForbiddenException(RuntimeException ex, WebRequest webRequest) throws JsonProcessingException {
        return handleException(ex, webRequest, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest webRequest) throws JsonProcessingException {
        return handleException(ex, webRequest, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected ResponseEntity<Object> handleException(Exception ex, WebRequest request, HttpStatusCode status) throws JsonProcessingException {
        ProblemDetail responseBody = buildResponseBody(ex, (HttpStatus) status);
        LOGGER.error("[] : [{}], {}", status.value(), objectMapper.writeValueAsString(responseBody), ex);
        return super.handleExceptionInternal(ex, responseBody, EMPTY_HTTP_HEADERS, status, request);
    }

    protected ProblemDetail buildResponseBody(Exception ex, HttpStatus status) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(isEmpty(ex) ? "" : ex.getMessage());
        return problemDetail;
    }
}
