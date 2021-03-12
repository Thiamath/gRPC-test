package com.thiamath.user.app;

import com.thiamath.user.client.Controller;

public class AppContext {

    private final Controller controller;

    public AppContext(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }
}
