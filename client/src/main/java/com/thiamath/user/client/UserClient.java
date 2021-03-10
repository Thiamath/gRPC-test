package com.thiamath.user.client;

import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.Attribute;
import com.thiamath.user.UserOuterClass;
import com.thiamath.user.UserServiceGrpc;
import io.grpc.Channel;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserClient {
    private static final Logger logger = Logger.getLogger(UserClient.class.getName());

    private final UserServiceGrpc.UserServiceBlockingStub blockingStub;
    private final UserServiceGrpc.UserServiceStub asyncStub;

    public UserClient(Channel channel) {
        blockingStub = UserServiceGrpc.newBlockingStub(channel);
        asyncStub = UserServiceGrpc.newStub(channel);
    }

    public void sendUser(final UserOuterClass.User user) {
        final UserOuterClass.SendUserListResponse sendUserListResponse = blockingStub.sendUser(user);
        System.out.println(Ansi.colorize("Server received " + sendUserListResponse.getReceived() + " users", Attribute.YELLOW_TEXT()));
    }

    public void sendUserList(final Iterable<UserOuterClass.User> userList) {
        StreamObserver<UserOuterClass.SendUserListResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(UserOuterClass.SendUserListResponse value) {
                System.out.println(Ansi.colorize("Server received " + value.getReceived() + " users", Attribute.YELLOW_TEXT()));
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(Ansi.colorize("Server ERROR: " + t.getMessage(), Attribute.YELLOW_TEXT(), Attribute.RED_BACK()));
            }

            @Override
            public void onCompleted() {
                System.out.println(Ansi.colorize("Server finished reception", Attribute.YELLOW_TEXT()));
            }
        };
        StreamObserver<UserOuterClass.User> sendUserList = asyncStub.sendUserList(responseObserver);
        for (final UserOuterClass.User user : userList) {
            sendUserList.onNext(user);
        }
        sendUserList.onCompleted();
        System.out.println(Ansi.colorize("All users sent", Attribute.GREEN_TEXT()));
    }

    public UserOuterClass.User getUser(final String username) {
        UserOuterClass.GetUserRequest request = UserOuterClass.GetUserRequest.newBuilder()
                .setName(username)
                .build();
        UserOuterClass.User user = blockingStub.getUser(request);

        logger.info("Got user " + user.getName());
        return user;
    }

    public Iterable<UserOuterClass.User> getUserList() {
        UserOuterClass.Void request = UserOuterClass.Void.newBuilder().getDefaultInstanceForType();
        Iterator<UserOuterClass.User> userList = blockingStub.getUserList(request);
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(userList, Spliterator.IMMUTABLE), false)
                .collect(Collectors.toUnmodifiableList());
    }

    public void conversation() throws Exception {
        throw new Exception("Not yet implemented");
    }
}
