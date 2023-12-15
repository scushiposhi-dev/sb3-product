package com.example.sb3product.message;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;

import java.time.ZonedDateTime;

@Builder
@AllArgsConstructor
@Setter
public class Event<K,D>{

    public enum Type{CREATE,DELETE}
    private final K key;
    private final D data;
    private final Type eventType;
    private final ZonedDateTime eventCreatedAt;

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    public ZonedDateTime getEventCreatedAt() {
        return eventCreatedAt;
    }

    public K getKey() {
        return key;
    }

    public D getData() {
        return data;
    }

    public Type getEventType() {
        return eventType;
    }
}
