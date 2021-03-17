package com.thiamath.user.app;

import com.thiamath.user.client.Controller;
import org.jline.reader.LineReader;

public class AppContext {

    private final Controller controller;
    private LineReader lineReader;

    public AppContext(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    public LineReader getLineReader() {
        return lineReader;
    }

    public void setLineReader(LineReader lineReader) {
        this.lineReader = lineReader;
    }
}
