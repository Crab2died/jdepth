package com.github.datasort;

import java.util.Arrays;

/**
 * 插入排序/希尔排序
 *
 * @author : Crab2Died
 * 2017/12/13  10:02:18
 */
public class InsertSort {

    public static void sort(int[] data) {

        if (data.length <= 1)
            return;

        for (int i = 1; i < data.length; i++) {
            int key = data[i];

            int j = i - 1;

            while (j >= 0 && data[j] > key) {
                data[j + 1] = data[j];
                j--;
            }
            data[j + 1] = key;
        }
    }

    public static void main(String... args) {
        int[] data = {121, 12323, 23231, 121, 12, 2345, 23, 12334, 1343, 2232, 234, 32, 2434, 2456, 34352, 367, 83};
        sort(data);
        System.out.println(Arrays.toString(data));
    }
}

