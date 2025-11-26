package io.github.dfnabiullin.taskmanager.user.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserExceptionHandlerTest {
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private UserExceptionHandler exceptionHandler;

    @Test
    void handleUserNotFoundException_shouldReturnProblemDetailWithStatus404() {
        final var initialUserNotFoundException = new UserNotFoundException(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        final Locale locale = Locale.ENGLISH;

        final ProblemDetail expectedProblemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        expectedProblemDetail.setTitle("User Not Found");
        expectedProblemDetail.setDetail("User not found with uuid 123e4567-e89b-12d3-a456-426614174000");

        when(messageSource.getMessage("error.user.notFound", new Object[]{initialUserNotFoundException.getUuid()}, locale)).thenReturn("User not found with uuid 123e4567-e89b-12d3-a456-426614174000");

        final ProblemDetail actualProblemDetail = exceptionHandler.handleUserNotFoundException(initialUserNotFoundException, locale);

        assertEquals(expectedProblemDetail, actualProblemDetail);
    }

    @Test
    void handleMethodArgumentNotValid_whenSingleError_shouldReturnMessageWithoutSeparator() {
        final var initialBindingResult = new BeanPropertyBindingResult(new Object(), "object");
        initialBindingResult.addError(new ObjectError("object", "defaultMessage"));
        final var initialMethodArgumentNotValidException = new MethodArgumentNotValidException(null, initialBindingResult);
        final HttpHeaders initialHttpHeaders = HttpHeaders.EMPTY;
        final HttpStatusCode initialHttpStatusCode = HttpStatus.BAD_REQUEST;
        final WebRequest initialWebRequest = mock(WebRequest.class);

        final ProblemDetail expectedProblemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        expectedProblemDetail.setTitle("Method Argument Not Valid");
        expectedProblemDetail.setDetail("defaultMessage");
        final ResponseEntity<Object> expectedResponseEntity = ResponseEntity.badRequest()
                .headers(HttpHeaders.EMPTY)
                .body(expectedProblemDetail);

        final ResponseEntity<Object> actualResponseEntity = exceptionHandler.handleMethodArgumentNotValid(initialMethodArgumentNotValidException, initialHttpHeaders, initialHttpStatusCode, initialWebRequest);

        assertEquals(expectedResponseEntity, actualResponseEntity);
    }

    @Test
    void handleMethodArgumentNotValid_whenMultipleErrors_shouldReturnMessageWithSeparator() {
        final var initialBindingResult = new BeanPropertyBindingResult(new Object(), "object");
        initialBindingResult.addError(new ObjectError("object1", "defaultMessage1"));
        initialBindingResult.addError(new ObjectError("object2", "defaultMessage2"));
        final var initialMethodArgumentNotValidException = new MethodArgumentNotValidException(null, initialBindingResult);
        final HttpHeaders initialHttpHeaders = HttpHeaders.EMPTY;
        final HttpStatusCode initialHttpStatusCode = HttpStatus.BAD_REQUEST;
        final WebRequest initialWebRequest = mock(WebRequest.class);

        final ProblemDetail expectedProblemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        expectedProblemDetail.setTitle("Method Argument Not Valid");
        expectedProblemDetail.setDetail("defaultMessage1; defaultMessage2");
        final ResponseEntity<Object> expectedResponseEntity = ResponseEntity.badRequest()
                .headers(HttpHeaders.EMPTY)
                .body(expectedProblemDetail);

        final ResponseEntity<Object> actualResponseEntity = exceptionHandler.handleMethodArgumentNotValid(initialMethodArgumentNotValidException, initialHttpHeaders, initialHttpStatusCode, initialWebRequest);

        assertEquals(expectedResponseEntity, actualResponseEntity);
    }

    @Test
    void handleException_shouldReturnProblemDetailWithStatus500() {
        final Locale locale = Locale.ENGLISH;

        final ProblemDetail expectedProblemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        expectedProblemDetail.setTitle("Internal Server Error");
        expectedProblemDetail.setDetail("Internal server error");

        when(messageSource.getMessage("error.unexpected", null, locale)).thenReturn("Internal server error");

        final ProblemDetail actualProblemDetail = exceptionHandler.handleException(locale);

        assertEquals(expectedProblemDetail, actualProblemDetail);
    }
}