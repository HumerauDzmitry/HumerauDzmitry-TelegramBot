package com.telegram.bot.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.*;
import org.zalando.problem.spring.common.HttpStatusAdapter;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.violations.Violation;
import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807)
 */

@ControllerAdvice
@Slf4j
public class ExceptionTranslator implements ProblemHandling {

    private static final String FIELD_ERRORS_KEY = "fieldErrors";
    private static final String TYPE_ERROR_KEY = "typeErrors";
    private static final String PATH_KEY = "path";

    public static ThrowableProblem throwableToInternalProblem(Throwable t) {
        Objects.requireNonNull(t, "Throwable is NULL");
        List<Throwable> throwableList = ExceptionUtils.getThrowableList(t);
        Collections.reverse(throwableList);
        ThrowableProblem parent = null;
        for (Throwable throwable : throwableList) {
            parent = Problem.builder()
                    .withTitle("Internal Server Error")
                    .withStatus(Status.INTERNAL_SERVER_ERROR)
                    .withDetail(ExceptionUtils.getMessage(throwable))
                    .with("stacktrace", getStacktraceElements(throwable))
                    .withCause(parent)
                    .build();
        }
        return parent;
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<Violation> fieldErrors = result.getFieldErrors().stream()
                .map(f -> new Violation(f.getField(), f.getDefaultMessage()))
                .collect(Collectors.toList());

        List<ObjectError> globalError = result.getGlobalErrors();
        TypeViolation typeViolation = null;
        if (CollectionUtils.isNotEmpty(globalError)) {
            List<String> messages = result.getGlobalErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            typeViolation = TypeViolation.builder().objectName(result.getObjectName()).messages(messages).build();
        }

        Problem problem = Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withTitle(ErrorType.SYSTEM_VALIDATION_ERROR.name())
                .withDetail("Validation error")
                .with(FIELD_ERRORS_KEY, fieldErrors)
                .with(TYPE_ERROR_KEY, typeViolation)
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleNoSuchElementException(NoSuchElementException ex, NativeWebRequest request) {
        return create(request, ex, Status.NOT_FOUND);
    }

    @ExceptionHandler( Exception.class )
    public ResponseEntity<Problem> handleInternalServerError(NativeWebRequest request, Exception e) {
        //Проверка что пользуется @ResponseStatus над кастомными исключениями
        ResponseStatus annotation = findMergedAnnotation(e.getClass(), ResponseStatus.class);
        if (annotation != null) {
            return create(e,
                    Problem.builder()
                            .withStatus(new HttpStatusAdapter(annotation.value()))
                            .withDetail(annotation.reason() + ": " + e.getMessage())
                            .build(), request);
        }
        return create(e, throwableToInternalProblem(e), request);
    }

    private String getRequestPath(NativeWebRequest request) {
        return Optional.ofNullable(request.getNativeRequest(HttpServletRequest.class))
                .map(HttpServletRequest::getRequestURI)
                .orElse(StringUtils.EMPTY);
    }

    private static String[] getStacktraceElements(Throwable throwable) {
        return Arrays.stream(throwable.getStackTrace())
                .map(StackTraceElement::toString)
                .toArray(String[]::new);
    }

//    @Override
//    public void log(@ParametersAreNonnullByDefault final Throwable throwable, final Problem problem, final NativeWebRequest request, final HttpStatus status) {
//        LoggingUtils.log(throwable, status);
//    }

    private ResponseEntity<Problem> create(NativeWebRequest request, Exception ex, StatusType status) {
        Problem problem = Problem.builder()
                .withStatus(status)
                .withDetail(ex.getMessage())
                .build();
        return create(ex, problem, request);
    }
}
