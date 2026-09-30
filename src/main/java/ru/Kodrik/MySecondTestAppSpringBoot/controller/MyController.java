package ru.Kodrik.MySecondTestAppSpringBoot.controller;


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
import ru.Kodrik.MySecondTestAppSpringBoot.service.ModifyRequestService;
import ru.Kodrik.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.Kodrik.MySecondTestAppSpringBoot.service.UnsupportedCodeService;
import ru.Kodrik.MySecondTestAppSpringBoot.service.ValidationService;
import ru.Kodrik.MySecondTestAppSpringBoot.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final UnsupportedCodeService unsupportedCodeService;
    private final ModifyResponseService modifyResponseService;
    private final ModifyRequestService modifySystemNameRequestService;
    private final ModifyRequestService modifySourceRequestService;

    @Autowired
    public MyController(ValidationService validationService,
                        UnsupportedCodeService unsupportedCodeService,
                        @Qualifier("modifySystemTimeResponseService") ModifyResponseService modifyResponseService,
                        @Qualifier("modifySystemNameRequestService") ModifyRequestService modifySystemNameRequestService,
                        @Qualifier("modifySourceRequestService") ModifyRequestService modifySourceRequestService) {
        this.validationService = validationService;
        this.unsupportedCodeService = unsupportedCodeService;
        this.modifyResponseService = modifyResponseService;
        this.modifySystemNameRequestService = modifySystemNameRequestService;
        this.modifySourceRequestService = modifySourceRequestService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request, BindingResult bindingResult) {

        log.info("request: {}", request);

        // === ДОПОЛНИТЕЛЬНЫЙ ФУНКЦИОНАЛ ===
        // 1. Фиксируем время получения запроса Сервисом 1
        request.setTimestamp(LocalDateTime.now());

        // 2. Меняем поле source (локально, без отправки)
        modifySourceRequestService.modify(request);

        // 3. Меняем systemName и отправляем запрос в Сервис 2 (порт 8084)
        modifySystemNameRequestService.modify(request);
        // ===================================

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();

        log.info("response: {}", response);

        try {
            log.info("Запуск валидации запроса");
            validationService.isValid(bindingResult);
            log.info("Валидация запроса прошла успешно");

            log.info("Запуск проверки кода операции: {}", request.getOperationUid());
            unsupportedCodeService.check(request);
            log.info("Проверка кода операции прошла успешно");

        } catch (ValidationFailedException e) {
            log.error("Ошибка валидации запроса: {}", e.getMessage(), e);
            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(error ->
                        log.error("Ошибка bindingResult: {}", error.getDefaultMessage())
                );
            }
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            log.info("Изменён ответ (ошибка валидации): {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (UnsupportedCodeException e) {
            log.error("Неподдерживаемый код операции: {}", e.getMessage(), e);
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);
            log.info("Изменён ответ (неподдерживаемый код): {}", response);

        } catch (Exception e) {
            log.error("Неизвестная ошибка при обработке запроса: {}", e.getMessage(), e);
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.info("Изменён ответ (неизвестная ошибка): {}", response);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Запуск модификации ответа");
        modifyResponseService.modify(response);
        log.info("Ответ после модификации: {}", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}