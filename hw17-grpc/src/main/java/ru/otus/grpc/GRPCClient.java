package ru.otus.grpc;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.NumberRequest;
import ru.otus.protobuf.generated.NumbersServiceGrpc;

import java.util.concurrent.CountDownLatch;

public class GRPCClient {

    private static final Logger log = LoggerFactory.getLogger(GRPCClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {

        var managedChannel = ManagedChannelBuilder
                .forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var asyncClient = NumbersServiceGrpc.newStub(managedChannel);

        log.info("numbers Client is starting...");

        long currentValue = 0;
        long newValue;

        var latch = new CountDownLatch(1);

        var clientStreamObserver = new ClientStreamObserver(latch);
        asyncClient.getNumber(makeNumberRequest(), clientStreamObserver);

        for (int i = 0; i < 50; i++) {
            newValue = clientStreamObserver.getResponseNumber();
            currentValue = currentValue + newValue + 1;
            log.info("currentValue:{}", currentValue);
            sleep();
        }

        latch.await();

        log.info("numbers Client is shutting down...");
        managedChannel.shutdown();

    }

    private static NumberRequest makeNumberRequest() {
        return NumberRequest.newBuilder()
                .setFirstValue(0)
                .setLastValue(30)
                .build();
    }

    private static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
