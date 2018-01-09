package com.github.arithmetic;

/**
 * 检查字符串是否有重复的字符
 */
public class CheckStringRepetitiveChar {

    public static void main(String... args) {
        String str = "this is string";

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            for (int j = i + 1; j < str.length(); j++){
                if (ch == str.charAt(j)){
                    System.out.println("有相同的字符");
                    return;
                }
            }
        }
    }
}
