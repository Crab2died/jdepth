package com.github.gc;

public class GCAllocation {

    public static void main(String[] args){
        /*
         * -XX:+PrintGCDetails -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8
         */
        byte[] byte1 = new byte[2 * 1024 * 1024];
        byte[] byte2 = new byte[2 * 1024 * 1024];
        byte[] byte3 = new byte[2 * 1024 * 1024];
        byte[] byte4 = new byte[4 * 1024 * 1024];
    }
}
