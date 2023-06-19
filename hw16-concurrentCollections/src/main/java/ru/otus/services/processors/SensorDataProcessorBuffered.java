package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.lib.SensorDataBufferedWriter;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;

    private final Set<SensorData> dataBuffer = new ConcurrentSkipListSet<>(Comparator.comparing(SensorData::getMeasurementTime));

    private final Lock lock = new ReentrantLock();
    private final SensorDataBufferedWriter writer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
    }

    @Override
    public void process(SensorData data) {
        lock.lock();
        try {
            dataBuffer.add(data);

            if (dataBuffer.size() >= bufferSize) {
                flush();
            }
        } finally {
            lock.unlock();
        }
    }

    public void flush() {
        try {
            lock.lock();
            if (!dataBuffer.isEmpty()) {
                writer.writeBufferedData(new ArrayList<>(dataBuffer));
                dataBuffer.clear();
            }
        } catch (Exception exception) {
            log.error("exception", exception);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
