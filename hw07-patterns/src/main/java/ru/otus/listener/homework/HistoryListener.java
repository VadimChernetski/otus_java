package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Deque<Memento>> history = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        if (history.get(msg.getId()) == null) {
            Deque<Memento> deque = new ArrayDeque<>();
            deque.add(new Memento(LocalDateTime.now(), msg.deepClone()));
            history.put(msg.getId(), deque);
        } else {
            history.get(msg.getId()).add(new Memento(LocalDateTime.now(), msg.deepClone()));
        }
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        final Deque<Memento> deque = history.get(id);
        if (deque == null || deque.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(deque.pop().state());
        }
    }
}
