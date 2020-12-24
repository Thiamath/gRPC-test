package com.thiamath.user.service;

import io.grpc.stub.StreamObserver;
import test.thiamath.user.UserOuterClass;
import test.thiamath.user.UserServiceGrpc.UserServiceImplBase;

import java.util.logging.Logger;

public class UserService extends UserServiceImplBase {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Override
    public void getUser(UserOuterClass.User request, StreamObserver<UserOuterClass.User> responseObserver) {
        logger.info("User requested " + request.getName());
        responseObserver.onNext(request);
        responseObserver.onCompleted();
    }
}
