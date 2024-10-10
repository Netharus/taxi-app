package com.example.passenger.handler;

import com.example.passenger.dto.ExceptionMessage;
import com.example.passenger.exceptions.ResourceNotFound;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import org.apache.coyote.BadRequestException;

import java.io.IOException;
import java.io.InputStream;

public class RetreiveMessageErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        ExceptionMessage message;
        try (InputStream bodyIs = response.body()
                .asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, ExceptionMessage.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        return switch (response.status()) {
            case 400 -> new BadRequestException(message.message() != null ? message.message() : "Bad Request");
            case 404 -> new ResourceNotFound(message.message() != null ? message.message() : "Not found");
            case 401 ->
                    new RetryableException(response.status(), response.reason(), response.request().httpMethod(), (Long) null, response.request());
            default -> errorDecoder.decode(methodKey, response);
        };
    }
}
