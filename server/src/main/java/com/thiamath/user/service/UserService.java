package com.thiamath.user.service;

import io.grpc.stub.StreamObserver;
import test.thiamath.user.UserOuterClass;
import test.thiamath.user.UserServiceGrpc.UserServiceImplBase;

public class UserService extends UserServiceImplBase {
    @Override
    public void getUser(UserOuterClass.User request, StreamObserver<UserOuterClass.User> responseObserver) {
        responseObserver.onNext(UserOuterClass.User.newBuilder().build());
        responseObserver.onCompleted();
    }
}
