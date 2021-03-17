package com.thiamath.user.service;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class UserServer {

    private final Server server;

    /**
     * Create a User server using serverBuilder as a base and features as data.
     */
    public UserServer(int port) {
        server = ServerBuilder.forPort(port).addService(new UserService()).build();
    }

    /**
     * Main method.  This comment makes the linter happy.
     */
    public static void main(String[] args) throws Exception {
        AnsiConsole.systemInstall();
        UserServer server = new UserServer(8980);
        server.start();
        server.blockUntilShutdown();
        AnsiConsole.systemUninstall();
    }

    public void start() throws IOException {
        server.start();
        System.out.println("Server started, listening on " + server.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                UserServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));
    }

    /**
     * Stop serving requests and shutdown resources.
     */
    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
