package com.github.arithmetic;

public class LinkedBitMap {


    private final Node node0 = new Node();

    private Node leftRootNode = node0;

    private Node rightRootNode = node0;

    private int leftLen = 0;

    private int rightLen = 1;


    private static class Node {

        private byte entry = 0x00000000;

        private Node left;

        private Node right;

    }

    public void set(int bit) {
        int index;
        if (bit < 0) {
            bit = -bit;
            index = bit / 8;
            bit = bit % 8;
            if (leftLen < index) {
                for (int left = leftLen; left < index; left++) {
                    Node node = new Node();
                    leftRootNode.left = node;
                    leftRootNode = node;
                    leftLen++;
                    if (left == index - 1)
                        node.entry = (byte) bit;
                }
            } else {
                Node node = node0;
                for (; index < 0; index--) {
                    node = node.left;
                }
                node.entry |= (byte) bit;
            }
        } else {
            index = bit / 8;
            bit = bit % 8;
            if (rightLen < index) {
                for (int right = rightLen; right < index; right++) {
                    Node node = new Node();
                    rightRootNode.right = node;
                    rightRootNode = node;
                    rightLen++;
                    if (right == index - 1) {
                        node.entry = (byte) bit;
                    }
                }
            } else {
                Node node = node0;
                for (; index < 1; index--) {
                    node = node.right;
                }
                node.entry |= (byte) bit;
            }
        }
    }

    public boolean get(int bit) {

        int index;
        if (bit < 0) {
            bit = -bit;
            index = bit / 8;
            bit = bit % 8;
            if (leftLen < index) { return false; }
            else {
                Node node = node0;
                for (; index < 0; index--) {
                    node = node.left;
                }
                return (node.entry & (byte) bit) == (byte) bit;
            }

        }else {
            index = bit / 8;
            bit = bit % 8;
            if (leftLen < index) { return false; }
            else {
                Node node = node0;
                for (; index < 1; index--) {
                    node = node.right;
                }
                return (node.entry & (byte) bit) == (byte) bit;
            }
        }
    }



    public static void main(String... args) {
        LinkedBitMap linkedBitMap = new LinkedBitMap();
        linkedBitMap.set(-1019);
        linkedBitMap.set(-12);
        linkedBitMap.set(19);
        linkedBitMap.set(617);
        linkedBitMap.set(98);
        System.out.println(linkedBitMap.get(1212));
        System.out.println(linkedBitMap.get(-1019));
        System.out.println(linkedBitMap.get(-12));
        System.out.println(linkedBitMap.get(617));
        System.out.println(linkedBitMap.get(98));
    }

}
