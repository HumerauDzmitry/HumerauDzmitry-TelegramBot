package com.telegram.bot.exception;

import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;
import org.zalando.problem.violations.Violation;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Supplier;
public class ProblemSupplier {

    public static final String FIELD_ERRORS_KEY = "fieldErrors";

    public static <T> T badRequestOnNull(T dto, String message) {
        return Optional.ofNullable(dto).orElseThrow(() -> badRequest(message));
    }

    public static ThrowableProblem badRequest(String message, Object... param) {
        return buildProblem(Status.BAD_REQUEST, "Invalid request parameters", message, param);
    }

    public static ThrowableProblem notFound(String message, Object... param) {
        return buildProblem(Status.NOT_FOUND, "Not found", message, param);
    }

    public static ThrowableProblem illegalState(String message, Object... param) {
        return buildProblem(Status.CONFLICT, "Request rejected", message, param);
    }

    public static ThrowableProblem serverError(String message, Object... param) {
        return buildProblem(Status.INTERNAL_SERVER_ERROR, "Service error", message, param);
    }

    public static ThrowableProblem forbidden(String message, Object... param) {
        return buildProblem(Status.FORBIDDEN, "The request is not allowed", message, param);
    }

    public static ThrowableProblem badGateway(String message, Object... param) {
        return buildProblem(Status.BAD_GATEWAY, "External system error", message, param);
    }

    public static ThrowableProblem serviceUnavailable(String message, Object... param) {
        return buildProblem(Status.SERVICE_UNAVAILABLE, "Service unavailable", message, param);
    }

    public static ThrowableProblem unsupportedMediaType(String message, Object... param) {
        return buildProblem(Status.UNSUPPORTED_MEDIA_TYPE, "Unsupported format", message, param);
    }

    public static ThrowableProblem tooLargeContent(String message, Object... param) {
        return buildProblem(Status.REQUEST_ENTITY_TOO_LARGE, "Message size exceeded", message, param);
    }

    public static ThrowableProblem unauthorized(String message, Object... param) {
        return buildProblem(Status.UNAUTHORIZED, "Access is denied", message, param);
    }

    public static ThrowableProblem buildProblem(Status status, String title, String message, Object... param) {
        return Problem.builder()
                .withStatus(status)
                .withTitle(title)
                .withDetail(message != null ? String.format(message, param) : "null")
                .build();
    }

    public static ThrowableProblem onField(String field, Supplier<ThrowableProblem> supplier) {
        ThrowableProblem problem = supplier.get();
        return Problem.builder()
                .withStatus(problem.getStatus())
                .withTitle(problem.getTitle())
                .withDetail(problem.getMessage())
                .with(FIELD_ERRORS_KEY, Collections.singletonList(new Violation(field, problem.getMessage())))
                .build();
    }

    public static void throwProblem(ThrowableProblem problem) {
        throw problem;
    }

    private ProblemSupplier() {
        //NOOP
    }
}
