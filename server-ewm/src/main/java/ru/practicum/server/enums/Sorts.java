package ru.practicum.server.enums;

public enum Sorts {
    EVENT_DATE, VIEWS;

    public static Sorts fromString(String state) {
        return Sorts.valueOf(state);
    }
}