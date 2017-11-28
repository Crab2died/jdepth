package com.github.jvm.execengine;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MethodInvokeProxy {

    private static final String FINAL = "FINAL";

    private double sum(int arg1, double arg2){
        System.out.println(arg1 + arg2);
        return arg1 + arg2;
    }


    public static void main(String[] args){
        MethodType mt = MethodType.methodType(double.class, int.class, double.class);
        MethodHandle mh = null;
        try {
            MethodInvokeProxy test = new MethodInvokeProxy();
            mh = MethodHandles.lookup().findSpecial(
                    MethodInvokeProxy.class,
                    "sum",
                    mt,
                    MethodInvokeProxy.class);
            mh.invoke(test, 1, 2);
            // 严格匹配
            double d = (double)mh.invokeExact(test, 1, 2.0);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }
}
