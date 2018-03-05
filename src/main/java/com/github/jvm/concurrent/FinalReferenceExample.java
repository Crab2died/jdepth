package com.github.jvm.concurrent;

// final域的引用类型
public class FinalReferenceExample {

    private final int[] intArray; // final是引用类型
    private static FinalReferenceExample obj;

    // 对于引用类型，写final域的重排序规则对编译器和处理器增加了如下约束：
    // 在构造函数内对一个final引用的对象的成员域的写入，与随后在构造函数外
    // 把这个被构造对象的引用赋值给一个引用变量，这两个操作之间不能重排序。
    public FinalReferenceExample() { // 构造函数
        intArray = new int[1]; // 1
        intArray[0] = 1; // 2
    }

    public static void writerOne() { // 写线程A执行
        obj = new FinalReferenceExample(); // 3
    }

    public static void writerTwo() { // 写线程B执行
        obj.intArray[0] = 2; // 4
    }

    public static void reader() { // 读线程C执行
        if (obj != null) { // 5
            int temp1 = obj.intArray[0]; // 6
            System.out.println(temp1);
        }
    }

    public static void main(String... args) {
        Thread th0 = new Thread(FinalReferenceExample::writerOne);
        Thread th1 = new Thread(FinalReferenceExample::writerTwo);
        Thread th2 = new Thread(FinalReferenceExample::reader);

        // th2保证了th0已完全执行，但由于与th1存在竞争关系，所以不保证能读到th1修改后的数据
        th0.start();
        th1.start();
        th2.start();
    }
}