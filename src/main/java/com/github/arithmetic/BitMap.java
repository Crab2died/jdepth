package com.github.arithmetic;

public class BitMap {


    private final Node node0 = new Node();

    private Node leftRootNode = node0;

    private Node rightRootNode = node0;

    private int leftLen = 0;

    private int rightLen = 1;


    private static class Node {

        private byte entry = 0x00000000;

        private Node next;

        private Node prev;

    }

    public void set(int bit) {
        int index = 1;
        if (bit < 0) {
            bit = -bit;
            for (; bit > (1 << 8); bit = bit >> 8 ) {
                index++;
            }
            if (leftLen < index) {
                for (int left = leftLen; left < index; left++) {
                    Node node = new Node();
                    leftRootNode.next = node;
                    leftRootNode = node;
                    leftLen ++;
                    if (left == index - 1)
                        node.entry = (byte) bit;
                }
            } else {
                Node node = node0;
                for (int left = 0; left < index; left++) {
                    node = node.next;
                }
                node.entry |= (byte) bit;
            }
        }
    }


    public static void main(String... args) {
        BitMap bitMap = new BitMap();
        bitMap.set(-1019);
        bitMap.set(-12);
    }

}
