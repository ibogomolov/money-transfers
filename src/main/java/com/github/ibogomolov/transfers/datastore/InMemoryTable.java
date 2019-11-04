package com.github.ibogomolov.transfers.datastore;

import lombok.val;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTable<T extends Identifiable> {

    private AtomicLong idGenerator = new AtomicLong(1);
    private Map<Long, T> elements = new ConcurrentHashMap<>();

    public T insert(T element) {
        val id = idGenerator.getAndIncrement();
        element.setId(id);
        elements.put(id, element);
        return element;
    }

    public T findById(Long id) {
        return elements.get(id);
    }
}
