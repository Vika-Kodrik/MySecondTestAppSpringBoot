package ru.Kodrik.MySecondTestAppSpringBoot.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class Response {

    @NotBlank(message = "Уникальный идентификатор сообщение. Обязательное поле для заполнения")
    private String uid;

    @NotBlank(message = "Уникальный идентификатор сообщение. Обязательное поле для заполнения")
    private String operationUid;

    @NotBlank(message = "Имя системы отправителя. Обязательное поле для заполнения")
    private String systemName;

    @NotBlank(message = "Время создания сообщения. Обязательное поле для заполнения")
    private String systemTime;

    @NotNull(message = "Код. Обязательное поле для заполнения")
    private Codes code;

    @NotNull(message = "Наименование ресурса. Обязательное поле для заполнения")
    private ErrorCodes errorCode;

    @NotNull(message = "Сообщение об ошибке. Обязательное поле для заполнения")
    private ErrorMessages errorMessage;

    private Double annualBonus;

}
