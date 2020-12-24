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

            client.getUser();
        } finally {
            channel.shutdownNow().awaitTermination(60, TimeUnit.SECONDS);
        }
    }
}
