package ru.otus.listener.homework;

import ru.otus.model.Message;

import java.time.LocalDateTime;

public record Memento(LocalDateTime localDateTime, Message state) {
}
