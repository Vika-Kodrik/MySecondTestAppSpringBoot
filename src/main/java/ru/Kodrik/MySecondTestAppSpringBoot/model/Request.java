package ru.Kodrik.MySecondTestAppSpringBoot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Request {

    @NotBlank(message = "UID не может быть пустым")
    private String uid;

    @NotBlank(message = "operationUid не может быть пустым")
    private String operationUid;

    @NotBlank(message = "Имя системы отправителя. Обязательное поле для заполнения")
    private String systemName;

    private String systemTime;

    private String source;
    private  Positions positions;
    private Double salary;
    private Double bonus;
    private Integer workDays;

    @Min(value = 1, message = "communicationId должен быть не меньше 1")
    @Max(value = 1000000, message = "communicationId должен быть не больше 1000000")
    private Integer communicationId;

    private Integer templateId;

    private Integer productCode;

    private Integer smsCode;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;

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
