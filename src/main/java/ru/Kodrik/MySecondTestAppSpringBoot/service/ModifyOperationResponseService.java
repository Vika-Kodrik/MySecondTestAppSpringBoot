package ru.Kodrik.MySecondTestAppSpringBoot.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.Kodrik.MySecondTestAppSpringBoot.model.Response;
import java.util.UUID;

@Service
@Qualifier("ModifyOperationResponseService")

public class ModifyOperationResponseService implements ModifyResponseService{
    @Override
    public Response modify(Response response) {

        UUID uid  = UUID.randomUUID();

        response.setOperationUid(uid.toString());
        return response;
    }
}
