package com.github.arithmetic;

/**
 * 一个数组移除指定的值，如[1,2,3,1,2,1]移除1数值后数组变为[2,3,2,0,0,0]整个过程不能新建数组
 */
public class ArraySweepSort {

    public static void main(String... args) {

        int[] arr = new int[]{3, 3, 1, 3, 2, 3, 2, 4, 5, 7, 6, 3, 9};
        int sweep = 3;
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (sweep != arr[i]) {
                arr[count] = arr[i];
                count++;
            }
        }
        for (int j = 0; j < count; j ++)
            System.out.println(arr[j]);
    }
}
