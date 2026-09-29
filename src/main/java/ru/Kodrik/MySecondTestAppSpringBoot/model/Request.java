package ru.Kodrik.MySecondTestAppSpringBoot.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Request {

    @NotBlank(message = "обязательное поле для заполнения")
    private String uid;

    @NotBlank(message = "обязательное поле для заполнения")
    private String operationUid;


    private Systems systemName;

    @NotBlank(message = "обязательное поле")
    private String systemTime;

    private String source;

    @Min(value = 1, message = "communicationId должен быть не меньше 1")
    @Max(value = 1000000, message = "communicationId должен быть не больше 1000000")
    private int communicationId;

    private int templateId;
    private int productCode;
    private int smsCode;

    @Override
    public String toString() {
        return "{" +
                "uid='" + uid + '\'' +
                ", operationUid='" + operationUid + '\'' +
                ", systemName='" + systemName + '\'' +
                ", systemTime='" + systemTime + '\'' +
                ", source='" + source + '\'' +
                ", communicationId=" + communicationId +
                ", templateId=" + templateId +
                ", productCode=" + productCode +
                ", smsCode=" + smsCode +
                '}';
    }
}
