package com.github.jvm.gc;

/**
 *  用完的对象设置为null
 *
 *  验证
 */
public class GCAllocation {

    public static void main(String[] args){
        /*
         * 试验一
         * -XX:+PrintGCDetails -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8
         */
//        byte[] byte1 = new byte[2 * 1024 * 1024];
//        byte[] byte2 = new byte[2 * 1024 * 1024];
//        byte[] byte3 = new byte[2 * 1024 * 1024];
//        byte[] byte4 = new byte[4 * 1024 * 1024];

        /*
         *  试验二
         *  证明对象手动设置为null的作用
         *  -XX:+PrintGCDetails -Xms10M -Xmx10M -Xmn10M -XX:SurvivorRatio=8
         *  -Xcomp 强制虚拟机使用编译模式也可以消除OOM,不建议
         */
        byte[] byte1 = new byte[1 * 1024 * 1024];
        byte[] byte2 = new byte[2 * 1024 * 1024];
        System.out.println(byte1.length + byte2.length);
        /*
         *  手动设置为null
         *  注释掉会报OOM
         */
        byte2 = null;
        byte[] byte3 = new byte[3 * 1024 * 1024];
        System.out.println(byte3.length);
        byte[] byte4 = new byte[4 * 1024 * 1024];


    }
}
