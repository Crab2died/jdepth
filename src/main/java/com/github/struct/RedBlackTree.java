package com.github.struct;

/**
 * 红黑树
 *
 * @author : Crab2Died
 * 2018/01/05  14:27:44
 */
public class RedBlackTree<T> {

    private Node<T> root;

    private static final boolean RED   = Boolean.FALSE;

    private static final boolean BLACK = Boolean.TRUE;

    private class Node<E> {

        // 颜色
        private boolean color;

        // 元素
        private E item;

        // 父节点
        private Node<E> parent;

        // 左节点
        private Node<E> left;

        // 右节点
        private Node<E> right;

        public Node(boolean color, E item, Node<E> parent, Node<E> left, Node<E> right) {
            this.color = color;
            this.item = item;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

    }


    public RedBlackTree() {
    }


    /*
     *      root                            root
     *       /                               /
     *      x                               y
     *     / \          --左旋-->           / \
     *    lx  y                           x   ry
     *       / \                         / \
     *      ly ry                       lx ly
     */
    private void leftRotate(Node<T> x) {
        //
        Node<T> y = x.right;

        x.right = y.left;

        if (null != y.left)
            y.left.parent = x;

        y.parent = x.parent;

        if (null == x.parent) {
            this.root = y;
        } else {
            if (y == x.parent.left)
                x.parent.left = y;
            else
                x.parent.right = y;
        }

        y.left = x;

        x.parent = y;

    }

    /*
     *         root                             root
     *          /                                /
     *         y                                x
     *        / \           --右旋-->           / \
     *       x   ry                           lx  y
     *      / \                                  / \
     *     lx  rx                               rx  ry
     */
    private void rightRotate(Node<T> y) {

        Node<T> x = y.left;

        y.left = x.right;

        if (null != x.right)
            x.right.parent = y;

        x.parent = y.parent;

        if (null == y.parent) {
            this.root = x;
        } else {
            if (y == y.parent.right)
                y.parent.right = x;
            else
                y.parent.left = x;
        }

        x.right = y;

        y.parent = x;
    }
}
