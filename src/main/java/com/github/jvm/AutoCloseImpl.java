package com.github.jvm;

import java.util.Arrays;

public class AutoCloseImpl implements AutoCloseable {

    private Byte[] bytes;

    @Override
    public void close() {
        if (this.bytes != null) {
            this.bytes = null;
        }
    }

    public AutoCloseImpl() {
    }

    public AutoCloseImpl(Byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "AutoCloseImpl{" +
                "bytes=" + Arrays.toString(bytes) +
                '}';
    }

    public static void main(String[] args) {

        try (AutoCloseImpl impl = new AutoCloseImpl(new Byte[]{'a', 'b'})) {
            System.out.println(impl);
        }
    }
}
