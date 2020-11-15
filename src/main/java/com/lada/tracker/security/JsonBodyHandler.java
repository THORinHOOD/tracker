package com.lada.tracker.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lada.tracker.services.models.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

//
public class JsonBodyHandler<W> implements HttpResponse.BodyHandler<Supplier<Response<W>>> {

    @Override
    public HttpResponse.BodySubscriber<Supplier<Response<W>>> apply(HttpResponse.ResponseInfo responseInfo) {
        return asJSON();
    }

    public static <W> HttpResponse.BodySubscriber<Supplier<Response<W>>> asJSON() {
        HttpResponse.BodySubscriber<InputStream> upstream = HttpResponse.BodySubscribers.ofInputStream();

        return HttpResponse.BodySubscribers.mapping(
                upstream,
                JsonBodyHandler::toSupplierOfType);
    }

    public static <W> Supplier<Response<W>> toSupplierOfType(InputStream inputStream) {
        return () -> {
            try (InputStream stream = inputStream) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(stream, new TypeReference<Response<W>>() {});
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }
}