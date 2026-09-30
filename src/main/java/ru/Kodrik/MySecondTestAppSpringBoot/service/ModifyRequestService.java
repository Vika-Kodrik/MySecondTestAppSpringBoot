package ru.Kodrik.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.Kodrik.MySecondTestAppSpringBoot.model.Request;

@Service
public interface ModifyRequestService {
    void modify(Request request);
}
