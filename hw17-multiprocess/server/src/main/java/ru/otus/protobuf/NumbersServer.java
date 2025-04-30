package ru.otus.protobuf;

import io.grpc.ServerBuilder;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.service.RemoteIncrementorImpl;

@SuppressWarnings({"squid:S106"})
public class NumbersServer {
    private static final Logger log = LoggerFactory.getLogger(NumbersServer.class);

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        var remoteDBService = new RemoteIncrementorImpl();

        var server =
                ServerBuilder.forPort(SERVER_PORT).addService(remoteDBService).build();
        server.start();
        log.info("Server waiting for client connections...");
        server.awaitTermination();
    }
}
