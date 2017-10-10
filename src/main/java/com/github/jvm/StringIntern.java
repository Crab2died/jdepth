package com.github.jvm;

/**
 * {@link String#intern()}
 */
public class StringIntern {

    public static void main(String[] args) {
        String s1 = "11";
        String s2 = new String("1") + new String("1");
        String s3 = s2.intern();
        String s4 = s2;
        System.out.println(s1 == s3);
        System.out.println(s1 == s4);
    }
}
