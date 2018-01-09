package com.github.datasort;

import java.util.Arrays;

/**
 * 快速排序
 *
 * @author : Crab2Died
 * 2017/12/13  10:00:24
 */
public class FastSort {

    private static int partition(int[] data, int low, int high) {

        int key = data[low];

        while (low < high) {
            while (key <= data[high] && low < high) {
                high--;
            }
            data[low] = data[high];

            while (key >= data[low] && low < high) {
                low++;
            }

            data[high] = data[low];
        }

        data[high] = key;

        return high;
    }

    public static void sort(int[] data, int low, int high) {

        if (low >= high)
            return;

        int tag = partition(data, low, high);

        sort(data, low, tag - 1);
        sort(data, tag + 1, high);
    }

    public static void main(String... args) {
        int[] data = {121, 12323, 23231, 121, 12, 2345, 23, 12334, 1343, 2232, 234, 32, 2434, 2456, 34352, 367, 83};
        sort(data, 0, data.length - 1);
        System.out.println(Arrays.toString(data));
    }

}
