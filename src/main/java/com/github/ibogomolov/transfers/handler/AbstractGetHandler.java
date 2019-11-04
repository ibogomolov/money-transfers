package com.github.ibogomolov.transfers.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ibogomolov.transfers.datastore.AppDatastore;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;

@RequiredArgsConstructor
public abstract class AbstractGetHandler<T> implements Route {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    protected final AppDatastore datastore;

    @Override
    public Object handle(Request request, Response response) throws JsonProcessingException {
        try {
            val id = Long.valueOf(request.params(":id"));
            response.type("application/json");
            response.status(HttpStatus.OK_200);
            T object = retrieve(id);
            if (object == null) {
                throw new IllegalArgumentException("Object does not exist.");
            }
            return objectMapper.writeValueAsString(object);
        } catch (IllegalArgumentException e) {
            response.type("text/plain");
            response.status(HttpStatus.NOT_FOUND_404);
            return e.getMessage();
        }
    }

    protected abstract T retrieve(Long id);
}
