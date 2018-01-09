package com.github.support;

public class G {

    public static G g1 = new G();

    public static G g2 = new G();

    public G() {
        System.out.println("构造函数");
    }

    static {
        System.out.println("静态块");
    }

    public static void main(String... args) {
        G g = new G();
    }
}
