package com.thiamath.user.service;

import com.thiamath.user.UserOuterClass.GetUserRequest;
import com.thiamath.user.UserOuterClass.User;
import com.thiamath.user.UserServiceGrpc.UserServiceImplBase;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class UserService extends UserServiceImplBase {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Override
    public void getUser(GetUserRequest request, StreamObserver<User> responseObserver) {
        logger.info("User requested " + request.getName());
        responseObserver.onNext(User.newBuilder().setName(request.getName()).build());
        responseObserver.onCompleted();
    }
}
