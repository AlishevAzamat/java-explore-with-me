package ru.practicum.server.enums;

public enum SortComment {
    NEW, OLD;

    public static SortComment fromString(String state) {
        return SortComment.valueOf(state);
    }
}