package com.github.arithmetic;

import java.util.Arrays;

/**
 * 数组数据倒置
 */
public class InversionData {

    public static void main(String... args) {

        int[] data = {1, 3, 2, 5, 6, 4, 1, 5, 2};
        int len = data.length;
        int temp;
        for (int i = 0; i < len / 2; i++) {
            temp = data[i];
            data[i] = data[len - i - 1];
            data[len - i - 1] = temp;
        }
        System.out.println(Arrays.toString(data));
    }
}
