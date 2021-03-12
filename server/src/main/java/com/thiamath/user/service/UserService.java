package com.thiamath.user.service;

import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.Attribute;
import com.thiamath.user.UserOuterClass;
import com.thiamath.user.UserOuterClass.ConversationStream;
import com.thiamath.user.UserOuterClass.GetUserRequest;
import com.thiamath.user.UserOuterClass.SendUserListResponse;
import com.thiamath.user.UserOuterClass.User;
import com.thiamath.user.UserServiceGrpc.UserServiceImplBase;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class UserService extends UserServiceImplBase {

    private final List<User> users;

    public UserService() {
        users = new ArrayList<>();
    }

    @Override
    public void getUser(GetUserRequest request, StreamObserver<User> responseObserver) {
        System.out.println(Ansi.colorize("Received User request ", Attribute.GREEN_TEXT())
                + Ansi.colorize(request.getName(), Attribute.CYAN_TEXT()));

//        users.stream().filter(user -> user.getName().equals(request.getName())).forEach(responseObserver::onNext);

        Optional<User> first = users.stream().filter(user -> user.getName().equals(request.getName())).findFirst();
        if (first.isPresent()) {
            responseObserver.onNext(first.get());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new StatusException(Status.NOT_FOUND.augmentDescription("User not found")));
        }
    }

    @Override
    public void getUserList(UserOuterClass.Void request, StreamObserver<User> responseObserver) {
        System.out.println(Ansi.colorize("Received getUserList request", Attribute.BLUE_TEXT()));
        for (User user : users) {
            responseObserver.onNext(user);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void sendUser(User request, StreamObserver<SendUserListResponse> responseObserver) {
        users.add(request);
        System.out.println(Ansi.colorize("Added single user " + request, Attribute.YELLOW_TEXT()));
        responseObserver.onNext(SendUserListResponse.newBuilder().setReceived(1).build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<User> sendUserList(StreamObserver<SendUserListResponse> responseObserver) {
        final AtomicInteger count = new AtomicInteger(0);
        return new StreamObserver<>() {
            @Override
            public void onNext(User user) {
                users.add(user);
                System.out.println(Ansi.colorize("Added user " + user, Attribute.YELLOW_TEXT()));
                count.incrementAndGet();
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(Ansi.colorize("ERROR: ", Attribute.RED_TEXT()) + t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(SendUserListResponse.newBuilder().setReceived(count.get()).build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<ConversationStream> conversation(StreamObserver<ConversationStream> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(ConversationStream value) {
                System.out.println(Ansi.colorize("Client -> " + value.getMessage(), Attribute.BRIGHT_GREEN_TEXT()));
                responseObserver.onNext(ConversationStream.newBuilder().setMessage("You said \"" + value.getMessage() + "\"").build());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(Ansi.colorize("ERROR: ", Attribute.RED_TEXT()) + t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
