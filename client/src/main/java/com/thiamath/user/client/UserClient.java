package com.thiamath.user.client;

import io.grpc.Channel;
import test.thiamath.user.UserOuterClass;
import test.thiamath.user.UserServiceGrpc;

import java.util.logging.Logger;

public class UserClient {
    private static final Logger logger = Logger.getLogger(UserClient.class.getName());

    private final UserServiceGrpc.UserServiceBlockingStub blockingStub;
//    private final UserServiceGrpc.UserServiceStub asyncStub;

    public UserClient(Channel channel) {
        blockingStub = UserServiceGrpc.newBlockingStub(channel);
//        asyncStub = UserServiceGrpc.newStub(channel);
    }

    public void getUser(final String username) {
        logger.info("Getting user");

        UserOuterClass.User request = UserOuterClass.User.newBuilder().setName(username).build();
        UserOuterClass.User user = blockingStub.getUser(request);

        logger.info("Got user " + user.getName());
    }
}
