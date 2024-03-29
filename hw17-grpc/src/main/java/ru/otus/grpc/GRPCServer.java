package ru.otus.grpc;


import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc.service.NumbersServiceImpl;

import java.io.IOException;

public class GRPCServer {

    private static final Logger log = LoggerFactory.getLogger(NumbersServiceImpl.class);
    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(new NumbersServiceImpl())
                .build();

        server.start();

        log.info("server waiting for client connections...");

        server.awaitTermination();
    }
}
