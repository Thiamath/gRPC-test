package com.thiamath.user.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        final String target = "localhost:8980";

        ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
        try {
            UserClient client = new UserClient(channel);

            for (int i = 0; i < 10; i++) {
                client.getUser("User_" + i);
                Thread.sleep(500);
            }
        } finally {
            channel.shutdownNow().awaitTermination(60, TimeUnit.SECONDS);
        }
    }
}
