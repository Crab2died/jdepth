package com.github.datasort;

import java.util.Arrays;

/**
 *  冒泡排序
 *
 *  @author : Crab2Died
 * 	2017/12/13  09:29:20
 */
public class BubbleSort {


    public static void sort(int[] data){

        int temp;
        for (int i = 1; i < data.length; i ++){
            for (int j = 0; j < data.length - i; j ++){
                if(data[j] < data[j + 1]){
                    temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                }
            }
        }
    }

    public static void main(String... args){
        int[] d = new int[]{121,12323,23231,121,23,12,23,1232434,13343,2232,234,32,2434,23456,34352,367,83};
        System.out.println("排序前: " + Arrays.toString(d));
        sort(d);
        System.out.println("排序后: " + Arrays.toString(d));
    }
}
