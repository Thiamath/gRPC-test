package com.thiamath.user.service;

import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.Attribute;
import com.thiamath.user.UserOuterClass.ConversationStream;
import com.thiamath.user.UserOuterClass.GetUserRequest;
import com.thiamath.user.UserOuterClass.SendUserListResponse;
import com.thiamath.user.UserOuterClass.User;
import com.thiamath.user.UserServiceGrpc.UserServiceImplBase;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class UserService extends UserServiceImplBase {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Override
    public void getUser(GetUserRequest request, StreamObserver<User> responseObserver) {
        System.out.println(Ansi.colorize("Received User request", Attribute.GREEN_TEXT()));
        responseObserver.onNext(User.newBuilder().setName(request.getName()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getUserList(GetUserRequest request, StreamObserver<User> responseObserver) {
        System.out.println(Ansi.colorize("Received getUserList request", Attribute.BLUE_TEXT()));
        for (int i = 0; i < 10; i++) {
            responseObserver.onNext(User.newBuilder().setName("Listed user " + i).build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<User> sendUserList(StreamObserver<SendUserListResponse> responseObserver) {
        final AtomicInteger count = new AtomicInteger(0);
        return new StreamObserver<>() {
            @Override
            public void onNext(User value) {
                count.incrementAndGet();
            }

            @Override
            public void onError(Throwable t) {

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
                responseObserver.onNext(ConversationStream.newBuilder().setMessage("You said \"" + value.getMessage() + "\"").build());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
