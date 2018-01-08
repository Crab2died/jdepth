package com.github.usefultool;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.IOException;

public class SignalCatch implements SignalHandler {

    @Override
    public void handle(Signal signal) {
        System.out.println(signal + " is received");
    }

    public static void main(String... args) throws IOException {
        SignalHandler handler = new SignalCatch();
        handler.handle(new Signal("TERM"));
        handler.handle(new Signal("SEGV"));
        handler.handle(new Signal("ILL"));
        handler.handle(new Signal("FPE"));
        handler.handle(new Signal("ABRT"));
        handler.handle(new Signal("INT"));
        handler.handle(new Signal("BREAK"));
    }
}
