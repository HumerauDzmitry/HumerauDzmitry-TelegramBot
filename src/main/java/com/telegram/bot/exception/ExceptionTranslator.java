package com.telegram.bot.exception;//package com.telegram.bot.tourist_assistant.exception;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang.exception.ExceptionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.context.support.DefaultMessageSourceResolvable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.zalando.problem.*;
//import org.zalando.problem.spring.common.HttpStatusAdapter;
//import org.zalando.problem.spring.common.MediaTypes;
//import org.zalando.problem.spring.web.advice.ProblemHandling;
//import org.zalando.problem.violations.ConstraintViolationProblem;
//import org.zalando.problem.violations.Violation;
//import ru.x5.wi.commons.starter.enums.ErrorType;
//import ru.x5.wi.commons.starter.exception.ClientProblem;
//import ru.x5.wi.commons.starter.exception.WIAuthenticationException;
//import ru.x5.wi.commons.starter.exception.WIExpiredJwtException;
//import ru.x5.wi.commons.starter.exception.WIMalformedJwtException;
//import ru.x5.wi.commons.starter.utils.LoggingUtils;
//import ru.x5.wi.commons.starter.utils.TokenExpiredStatus;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//import javax.annotation.ParametersAreNonnullByDefault;
//import javax.servlet.http.HttpServletRequest;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;
//import static ru.x5.wi.commons.starter.controller.error.ErrorConstants.BDPR_TYPE;
//
///**
// * Controller advice to translate the server side exceptions to client-friendly json structures.
// * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807)
// */
//@ControllerAdvice
//@Slf4j
//public class ExceptionTranslator implements ProblemHandling {
//
//    private static final String FIELD_ERRORS_KEY = "fieldErrors";
//    private static final String TYPE_ERROR_KEY = "typeErrors";
//    private static final String PATH_KEY = "path";
//
//    public static ThrowableProblem throwableToInternalProblem(Throwable t) {
//        Objects.requireNonNull(t, "Throwable is NULL");
//        List<Throwable> throwableList = ExceptionUtils.getThrowableList(t);
//        Collections.reverse(throwableList);
//        ThrowableProblem parent = null;
//        for (Throwable throwable : throwableList) {
//            parent = Problem.builder()
//                    .withTitle("Internal Server Error")
//                    .withStatus(Status.INTERNAL_SERVER_ERROR)
//                    .withDetail(ExceptionUtils.getMessage(throwable))
//                    .with("stacktrace", getStacktraceElements(throwable))
//                    .withCause(parent)
//                    .build();
//        }
//        return parent;
//    }
//
//    @Override
//    public Optional<MediaType> negotiate(final NativeWebRequest request) {
//        final Optional<MediaType> mediaType = ProblemHandling.super.negotiate(request);
//        return mediaType
//                .filter(MediaTypes.PROBLEM::equals)
//                .map(type -> MediaType.APPLICATION_PROBLEM_JSON)
//                .or(() -> mediaType);
//    }
//
//    /**
//     * Post-process the Problem payload to add the message key for the front-end if needed
//     */
//    @Override
//    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
//        if (entity == null || entity.getBody() == null) {
//            return entity;
//        }
//        Problem problem = entity.getBody();
//        if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
//            return entity;
//        }
//        ProblemBuilder builder = Problem.builder()
//                .withStatus(problem.getStatus())
//                .withTitle(problem.getTitle())
//                .withDetail(problem.getDetail())
//                .with(PATH_KEY, getRequestPath(request));
//        if (null == problem.getParameters().get(BDPR_TYPE)) {
//            builder.with(BDPR_TYPE, ErrorType.fromStatus(problem.getStatus()));
//        }
//        if (problem instanceof ConstraintViolationProblem) {
//            builder.with(FIELD_ERRORS_KEY, ((ConstraintViolationProblem) problem).getViolations());
//            return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
//        } else {
//            builder.withCause(((DefaultProblem) problem).getCause());
//            problem.getParameters().forEach(builder::with);
//            return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
//        }
//    }
//
//    @Override
//    public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {
//        BindingResult result = ex.getBindingResult();
//        List<Violation> fieldErrors = result.getFieldErrors().stream()
//                .map(f -> new Violation(f.getField(), f.getDefaultMessage()))
//                .collect(Collectors.toList());
//
//        List<ObjectError> globalError = result.getGlobalErrors();
//        TypeViolation typeViolation = null;
//        if (CollectionUtils.isNotEmpty(globalError)) {
//            List<String> messages = result.getGlobalErrors().stream()
//                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                    .collect(Collectors.toList());
//            typeViolation = TypeViolation.builder().objectName(result.getObjectName()).messages(messages).build();
//        }
//
//        Problem problem = Problem.builder()
//                .withStatus(Status.BAD_REQUEST)
//                .withTitle(ErrorType.SYSTEM_VALIDATION_ERROR.name())
//                .withDetail("Validation error")
//                .with(FIELD_ERRORS_KEY, fieldErrors)
//                .with(TYPE_ERROR_KEY, typeViolation)
//                .build();
//        return create(ex, problem, request);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<Problem> handleNoSuchElementException(NoSuchElementException ex, NativeWebRequest request) {
//        return create(request, ex, Status.NOT_FOUND);
//    }
//
//    @ExceptionHandler( WIExpiredJwtException.class )
//    public ResponseEntity<Problem> handleExpiredJwtException(NativeWebRequest request, WIExpiredJwtException ex) {
//        return create(request, ex, new TokenExpiredStatus());
//    }
//
//    @ExceptionHandler( WIMalformedJwtException.class )
//    public ResponseEntity<Problem> handleMalformedJwtException(NativeWebRequest request, WIMalformedJwtException ex) {
//        return create(request, ex, Status.BAD_REQUEST);
//    }
//
//    @ExceptionHandler( WIAuthenticationException.class )
//    public ResponseEntity<Problem> handleJwtException(NativeWebRequest request, WIAuthenticationException ex) {
//        return create(request, ex, Status.UNAUTHORIZED);
//    }
//
//    @ExceptionHandler( ClientProblem.class )
//    public ResponseEntity<Problem> handleClientProblem(NativeWebRequest request, ClientProblem problem) {
//        return create(problem, problem, request);
//    }
//
//    @ExceptionHandler( Exception.class )
//    public ResponseEntity<Problem> handleInternalServerError(NativeWebRequest request, Exception e) {
//        //Проверка что пользуется @ResponseStatus над кастомными исключениями
//        ResponseStatus annotation = findMergedAnnotation(e.getClass(), ResponseStatus.class);
//        if (annotation != null) {
//            return create(e,
//                    Problem.builder()
//                            .withStatus(new HttpStatusAdapter(annotation.value()))
//                            .withDetail(annotation.reason() + ": " + e.getMessage())
//                            .build(), request);
//        }
//        return create(e, throwableToInternalProblem(e), request);
//    }
//
//    private String getRequestPath(NativeWebRequest request) {
//        return Optional.ofNullable(request.getNativeRequest(HttpServletRequest.class))
//                .map(HttpServletRequest::getRequestURI)
//                .orElse(StringUtils.EMPTY);
//    }
//
//    private static String[] getStacktraceElements(Throwable throwable) {
//        return Arrays.stream(throwable.getStackTrace())
//                .map(StackTraceElement::toString)
//                .toArray(String[]::new);
//    }
//
//    @Override
//    public void log(@ParametersAreNonnullByDefault final Throwable throwable, final Problem problem, final NativeWebRequest request, final HttpStatus status) {
//        LoggingUtils.log(throwable, status);
//    }
//
//    private ResponseEntity<Problem> create(NativeWebRequest request, Exception ex, StatusType status) {
//        Problem problem = Problem.builder()
//                .withStatus(status)
//                .withDetail(ex.getMessage())
//                .build();
//        return create(ex, problem, request);
//    }
//}
