package ru.Kodrik.MySecondTestAppSpringBoot.service;


import org.springframework.stereotype.Service;
import ru.Kodrik.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.Kodrik.MySecondTestAppSpringBoot.model.Request;

@Service
public class UnsupportedCodeService {

    private static final String UNSUPPORTED_UID = "123";

    public void check(Request request) throws UnsupportedCodeException {
        if (UNSUPPORTED_UID.equals(request.getUid())) {
            throw new UnsupportedCodeException("uid = " + request.getUid() + " не поддерживается");
        }
    }
}
