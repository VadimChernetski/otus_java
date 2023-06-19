package ru.otus.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.NumberResponse;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public class ClientStreamObserver implements io.grpc.stub.StreamObserver<NumberResponse> {

    private static final Logger log = LoggerFactory.getLogger(ClientStreamObserver.class);
    private final CountDownLatch latch;
    private final Queue<Long> responseQueue = new ArrayDeque<>();

    public ClientStreamObserver(CountDownLatch latch) {
        this.latch = latch;
        sleep();
    }

    public long getResponseNumber() {
        return Optional.ofNullable(responseQueue.poll()).orElse(0L);
    }

    @Override
    public void onNext(NumberResponse value) {

        responseQueue.add(value.getValue());

        log.info("new value:{}", value.getValue());
    }

    @Override
    public void onError(Throwable t) {
        log.error("got error", t);
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        log.info("Data transfer completed");
        latch.countDown();
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
