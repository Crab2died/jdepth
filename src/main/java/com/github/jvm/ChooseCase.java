package com.github.jvm;

/**
 *  Linux权限赋值
 */
public class ChooseCase {

    // 执行
    public final static int CASE_1 = 0B001;

    // 写
    public final static int CASE_2 = 0B010;

    // 读
    public final static int CASE_3 = 0B100;

    public static void caseDone(int caser) {

        switch (caser) {
            case CASE_1:
                System.out.println(caser + ":执行权限");
                break;
            case CASE_2:
                System.out.println(caser + ":写权限");
                break;
            case CASE_3:
                System.out.println(caser + ":读权限");
                break;
            case CASE_1 | CASE_2:
                System.out.println(caser + ":执行&写权限");
                break;
            case CASE_1 | CASE_3:
                System.out.println(caser + ":执行&读权限");
                break;
            case CASE_2 | CASE_3:
                System.out.println(caser + ":读&写权限");
                break;
            case CASE_1 | CASE_2 | CASE_3:
                System.out.println(caser + ":执行&读&写权限");
                break;
            default:
                System.out.println(caser + ":无权限");
                break;
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 7; i++) {
            caseDone(i);
        }
    }
}
