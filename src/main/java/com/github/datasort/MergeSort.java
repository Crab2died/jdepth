package com.github.datasort;

import java.util.Arrays;

/**
 * 归并排序
 * 二路归并
 *
 * @author : Crab2Died
 * 2017/12/13  10:04:59
 */
public class MergeSort {

    private static void merge(int[] data, int head, int mid, int tail) {

        // 临时数组
        int[] tempArr = new int[data.length];

        // 右数组始发位置
        int r1 = mid + 1;

        int tempIndex = head;

        int l1 = head;

        while (head <= mid && r1 <= tail) {

            if (data[head] <= data[r1]) {
                tempArr[tempIndex++] = data[head++];
            } else {
                tempArr[tempIndex++] = data[r1++];
            }
        }

        while (r1 <= tail) {
            tempArr[tempIndex++] = data[r1++];
        }
        while (head <= mid) {
            tempArr[tempIndex++] = data[head++];
        }

        while (l1 <= tail) {
            data[l1] = tempArr[l1++];
        }
    }

    public static void sort(int[] data, int head, int tail) {

        if (head >= tail)
            return;

        int mid = (head + tail) / 2;

        sort(data, head, mid);
        sort(data, mid + 1, tail);

        merge(data, head, mid, tail);

    }


    public static void main(String... args) {

        // 只做归并
        int[] d = {1, 3, 5, 7, 9, 2, 4, 6, 8, 10};
        merge(d, 0, 4, d.length - 1);
        System.out.println(Arrays.toString(d));

        int[] data = {121, 12323, 23231, 121, 12, 2345, 23, 12334, 1343, 2232, 234, 32, 2434, 2456, 34352, 367, 83};
        sort(data, 0, data.length - 1);
        System.out.println(Arrays.toString(data));

    }

}
