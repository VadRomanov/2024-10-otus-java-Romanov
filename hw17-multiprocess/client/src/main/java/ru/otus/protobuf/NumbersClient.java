package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"squid:S106", "squid:S2142"})
public class NumbersClient {
    private static final Logger log = LoggerFactory.getLogger(NumbersClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    private static int currentValue;

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var latch = new CountDownLatch(1);
        var stub = RemoteIncrementorGrpc.newStub(channel);
        NumbersClient client = new NumbersClient();
        Thread thread = new Thread(client::action);
        thread.start();
        ClientMessage message =
                ClientMessage.newBuilder().setFirstValue(0).setLastValue(30).build();
        log.info("numbers Client is starting...");
        stub.getClients(message, new StreamObserver<ServerMessage>() {
            @Override
            public void onNext(ServerMessage serverMessage) {
                int value = serverMessage.getValue();
                log.info("new value:{} ", value);
                addValueToCurrent(value);
            }

            @Override
            public void onError(Throwable throwable) {
                log.error(String.valueOf(throwable));
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                log.info("request completed");
                latch.countDown();
            }
        });
        latch.await();
        thread.interrupt();
        channel.shutdown();
    }

    private void action() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1_000);
                addValueToCurrent(1);
                log.info("currentValue:{}", currentValue);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static synchronized void addValueToCurrent(int value) {
        currentValue += value;
    }
}
