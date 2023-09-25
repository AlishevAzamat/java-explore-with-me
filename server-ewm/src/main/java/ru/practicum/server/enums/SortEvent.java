package ru.practicum.server.enums;

public enum SortEvent {
    EVENT_DATE, VIEWS;

    public static SortEvent fromString(String state) {
        return SortEvent.valueOf(state);
    }
}