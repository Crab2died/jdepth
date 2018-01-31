package com.github.jvm;

import java.io.IOException;

public class FinalizerClass {

    private byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * 重写了{@link Object#finalize()}方法
     * 该类会进入{@link java.lang.ref.Finalizer}生命周期
     * 导致该对象在GC前会先通知执行该方法，该对象的GC回收将会延时到下一次或下几次GC后才得到回收
     */
    @Override
    protected void finalize() throws Throwable {
        System.out.println("class gc");
    }

    // -XX:+PrintGCDetails -Xms10M -Xmx10M -Xmn10M -XX:SurvivorRatio=8
    public static void main(String... args) throws IOException {
        FinalizerClass finalizerClass = new FinalizerClass();
        finalizerClass.setBytes(new byte[1024 * 1024 * 5]);
        finalizerClass = null;
        System.gc();
        // System.out.println(finalizerClass.getBytes().length);
        System.in.read();
    }
}
