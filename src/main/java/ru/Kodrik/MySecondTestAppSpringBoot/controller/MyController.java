package ru.Kodrik.MySecondTestAppSpringBoot.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.Kodrik.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.Kodrik.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.Kodrik.MySecondTestAppSpringBoot.model.*;
import ru.Kodrik.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.Kodrik.MySecondTestAppSpringBoot.service.UnsupportedCodeService;
import ru.Kodrik.MySecondTestAppSpringBoot.service.ValidationService;
import ru.Kodrik.MySecondTestAppSpringBoot.util.DateTimeUtil;

import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final UnsupportedCodeService unsupportedCodeService;
    private final ModifyResponseService modifyResponseService;

    @Autowired
    public MyController(ValidationService validationService,
                        UnsupportedCodeService unsupportedCodeService,
                        @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService) {
        this.validationService = validationService;
        this.unsupportedCodeService = unsupportedCodeService;
        this.modifyResponseService = modifyResponseService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request, BindingResult bindingResult) {

        log.info("request: {}", request);

        logTimeDifference(request);

        Response response = buildSuccessResponse(request);

        log.info("response: {}", response);

        try {
            validationService.isValid(bindingResult);
            unsupportedCodeService.check(request);
        } catch (ValidationFailedException e) {
            logError("Ошибка валидации запроса", e);
            logBindingErrors(bindingResult);
            return buildErrorResponse(response, Codes.FAILED,
                    ErrorCodes.VALIDATION_EXCEPTION, ErrorMessages.VALIDATION,
                    HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            logError("Неподдерживаемый код операции", e);
            applyError(response, Codes.FAILED,
                    ErrorCodes.UNSUPPORTED_EXCEPTION, ErrorMessages.UNSUPPORTED);
        } catch (Exception e) {
            logError("Неизвестная ошибка при обработке запроса", e);
            return buildErrorResponse(response, Codes.FAILED,
                    ErrorCodes.UNKNOWN_EXCEPTION, ErrorMessages.UNKNOWN,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Запуск модификации ответа");
        modifyResponseService.modify(response);
        log.info("Ответ после модификации: {}", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void logTimeDifference(Request request) {
        LocalDateTime receivedTime = LocalDateTime.now();
        if (request.getTimestamp() != null) {
            long diffMillis = ChronoUnit.MILLIS.between(request.getTimestamp(), receivedTime);
            log.info("Разница времени между получением запроса Сервисом 1 и Сервисом 2: {} мс", diffMillis);
        } else {
            log.warn("Поле timestamp отсутствует, невозможно вычислить разницу времени");
        }
    }

    private Response buildSuccessResponse(Request request) {
        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
        log.info("response: {}", response);
        return response;
    }

    private ResponseEntity<Response> buildErrorResponse(Response response, Codes code,
                                                        ErrorCodes errorCode, ErrorMessages errorMessage,
                                                        HttpStatus status) {
        applyError(response, code, errorCode, errorMessage);
        return new ResponseEntity<>(response, status);
    }

    private void applyError(Response response, Codes code, ErrorCodes errorCode, ErrorMessages errorMessage) {
        response.setCode(code);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        log.info("Изменён ответ: {}", response);
    }

    private void logError(String message, Exception e) {
        log.error("{}: {}", message, e.getMessage(), e);
    }

    private void logBindingErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error ->
                    log.error("Ошибка bindingResult: {}", error.getDefaultMessage()));
        }
    }
}