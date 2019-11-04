package com.github.ibogomolov.transfers.handler;

import com.github.ibogomolov.transfers.datamodel.MoneyTransfer;
import org.javatuples.Pair;
import org.junit.Test;
import org.mockito.Mockito;
import spark.Request;
import spark.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AbstractPostHandlerTest {

    @SuppressWarnings("unchecked")
    private void templateJsonParsingPositive(String json) {
        AbstractPostHandler handler = mock(AbstractPostHandler.class, Mockito.CALLS_REAL_METHODS);
        when(handler.getObjectClass()).thenReturn(MoneyTransfer.class);
        when(handler.process(any())).thenReturn(new Pair<>(123, "Test response."));

        final Request request = mock(Request.class);
        when(request.body()).thenReturn(json);

        final Response response = mock(Response.class);

        assertEquals("Test response.", handler.handle(request, response));
        verify(response).type("text/plain");
        verify(response).status(123);
    }

    @Test
    public void testJsonParsingPositive() {
        templateJsonParsingPositive(
                "{\"fromAccountId\": 1,\"toAccountId\": 2,\"amount\": 10,\"message\": \"message\"}");
        templateJsonParsingPositive(
                "{\"fromAccountId\": 1,\"toAccountId\": 2,\"amount\": 10.01,\"message\": \"message\",\"other\": \"field\"}");
    }

    private void templateJsonParsingNegative(String json, String expectedResponseBody) {
        AbstractPostHandler handler = mock(AbstractPostHandler.class, Mockito.CALLS_REAL_METHODS);
        when(handler.getObjectClass()).thenReturn(MoneyTransfer.class);

        final Request request = mock(Request.class);
        when(request.body()).thenReturn(json);

        final Response response = mock(Response.class);

        assertTrue(handler.handle(request, response).toString().contains(expectedResponseBody));
        verify(response).type("text/plain");
        verify(response).status(400);
    }

    @Test
    public void testJsonParsingNegativeMissingField() {
        templateJsonParsingNegative(
                "{\"*fromAccountId\": 1,\"toAccountId\": 2,\"amount\": 10,\"message\": \"message\"}",
                "Missing required creator property 'fromAccountId'");
        templateJsonParsingNegative(
                "{\"fromAccountId\": 1,\"*toAccountId\": 2,\"amount\": 10,\"message\": \"message\"}",
                "Missing required creator property 'toAccountId'");
        templateJsonParsingNegative(
                "{\"fromAccountId\": 1,\"toAccountId\": 2,\"*amount\": 10,\"message\": \"message\"}",
                "Missing required creator property 'amount'");
        templateJsonParsingNegative(
                "{\"fromAccountId\": 1,\"toAccountId\": 2,\"amount\": 10,\"*message\": \"message\"}",
                "Missing required creator property 'message'");
    }

    @Test
    public void testJsonParsingNegativeInvalidValue() {
        templateJsonParsingNegative(
                "{\"fromAccountId\": \"acc1\",\"toAccountId\": 2,\"amount\": 10,\"message\": \"message\"}",
                "Cannot deserialize value of type `java.lang.Long`");
        templateJsonParsingNegative(
                "{\"fromAccountId\": 1,\"toAccountId\": \"acc2\",\"amount\": 10,\"message\": \"message\"}",
                "Cannot deserialize value of type `java.lang.Long`");
        templateJsonParsingNegative(
                "{\"fromAccountId\": 1,\"toAccountId\": 2,\"amount\": \"ten\",\"message\": \"message\"}",
                "Cannot deserialize value of type `java.lang.Double`");
    }

    @Test
    public void testJsonParsingNegative() {
        templateJsonParsingNegative(
                "{\"fromAccountId\": null,\"toAccountId\": 2,\"amount\": 10,\"message\": \"message\"}",
                "Invalid `null` value encountered for property \"fromAccountId\"");
        templateJsonParsingNegative(
                "{\"fromAccountId\": 1,\"toAccountId\": null,\"amount\": 10,\"message\": \"message\"}",
                "Invalid `null` value encountered for property \"toAccountId\"");
        templateJsonParsingNegative(
                "{\"fromAccountId\": 1,\"toAccountId\": 2,\"amount\": null,\"message\": \"message\"}",
                "Invalid `null` value encountered for property \"amount\"");
        templateJsonParsingNegative(
                "{\"fromAccountId\": 1,\"toAccountId\": 2,\"amount\": 10,\"message\": null}",
                "Invalid `null` value encountered for property \"message\"");
    }
}