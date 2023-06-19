package ru.otus.grpc.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.protobuf.generated.NumberRequest;
import ru.otus.protobuf.generated.NumberResponse;
import ru.otus.protobuf.generated.NumbersServiceGrpc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class NumbersServiceImpl extends NumbersServiceGrpc.NumbersServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(NumbersServiceImpl.class);

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);


    @Override
    public void getNumber(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {
        log.info("request for the new sequence of numbers, firstValue:{}, lastValue:{}", request.getFirstValue(), request.getLastValue());
        var currentValue = new AtomicLong(request.getFirstValue());

        Runnable task = () -> {
            var newValue = currentValue.incrementAndGet();
            var response = NumberResponse.newBuilder().setValue(newValue).build();
            responseObserver.onNext(response);

            if (newValue == request.getLastValue()) {
                executor.shutdown();
                responseObserver.onCompleted();
                log.info("sequence of numbers finished");
            }

        };

        executor.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);

//        super.getNumber(request, responseObserver);
    }

}
