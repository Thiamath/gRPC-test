package com.thiamath.user.client;

import com.thiamath.user.UserOuterClass;
import com.thiamath.user.UserServiceGrpc;
import io.grpc.Channel;

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

        UserOuterClass.GetUserRequest request = UserOuterClass.GetUserRequest.newBuilder()
                .setName("Perry")
                .build();
        UserOuterClass.User user = blockingStub.getUser(request);

        logger.info("Got user " + user.getName());
    }
}
