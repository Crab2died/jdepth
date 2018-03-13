package com.github.jvm.asm;

import jdk.internal.org.objectweb.asm.util.ASMifier;

// ASM util
public class ASMUtils {

    // 已知class文件生成ASM代码
    public static void class2ASM(String clazz) throws Exception {
        ASMifier.main(new String[]{clazz});
    }

    public static void main(String... args) throws Exception {
        class2ASM("com.github.jvm.asm.CircleCalc");
    }
}
