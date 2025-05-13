package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.ClientMessage;
import ru.otus.protobuf.RemoteIncrementorGrpc;
import ru.otus.protobuf.ServerMessage;

@SuppressWarnings({"squid:S2142"})
public class RemoteIncrementorImpl extends RemoteIncrementorGrpc.RemoteIncrementorImplBase {
    private static final Logger log = LoggerFactory.getLogger(RemoteIncrementorImpl.class);

    @Override
    public void getClients(ClientMessage request, StreamObserver<ServerMessage> responseObserver) {
        int firstValue = request.getFirstValue();
        int lastValue = request.getLastValue();
        log.info("Range limits obtained: from {} to {}", firstValue, lastValue);

        for (int i = firstValue; i < lastValue; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
            int value = i + 1;
            log.info("Number to return to the client - {}", value);
            responseObserver.onNext(ServerMessage.newBuilder().setValue(value).build());
        }
        responseObserver.onCompleted();
    }
}
