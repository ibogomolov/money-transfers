package com.github.ibogomolov.transfers.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ibogomolov.transfers.datastore.AppDatastore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.eclipse.jetty.http.HttpStatus;
import org.javatuples.Pair;
import spark.Request;
import spark.Response;
import spark.Route;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractPostHandler<T> implements Route {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Getter
    private final Class<T> objectClass;
    protected final AppDatastore datastore;

    @Override
    public Object handle(Request request, Response response) {
        try {
            T object = objectMapper.readValue(request.body(), getObjectClass());
            val result = process(object);
            response.type("text/plain");
            response.status(result.getValue0());
            return result.getValue1();
        } catch (JsonProcessingException e) {
            response.type("text/plain");
            response.status(HttpStatus.BAD_REQUEST_400);
            return e.getMessage();
        }
    }

    protected abstract Pair<Integer, String> process(T object);
}
