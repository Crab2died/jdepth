package com.github.jvm.asm;


import com.github.jvm.classloader.ASMClassLoader;
import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;


public class BasicASM {


    public static byte[] generateClass() throws IOException {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, "com/github/jvm/asm/CircleCalc", null, "java/lang/Object", null);

        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "PI", "Ljava/lang/Double;", null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "calcArea", "(D)Ljava/lang/Double;", null, null);
            mv.visitCode();
            mv.visitVarInsn(DLOAD, 0);
            mv.visitInsn(DCONST_0);
            mv.visitInsn(DCMPG);
            Label l0 = new Label();
            mv.visitJumpInsn(IFGT, l0);
            mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "()V", false);
            mv.visitInsn(ATHROW);
            mv.visitLabel(l0);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitFieldInsn(GETSTATIC, "CircleCalc", "PI", "Ljava/lang/Double;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
            mv.visitVarInsn(DLOAD, 0);
            mv.visitInsn(DMUL);
            mv.visitVarInsn(DLOAD, 0);
            mv.visitInsn(DMUL);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(4, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            mv.visitCode();
            mv.visitLdcInsn(new Double("3.14"));
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
            mv.visitFieldInsn(PUTSTATIC, "CircleCalc", "PI", "Ljava/lang/Double;");
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 0);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }


    public static void main(String... args) throws Exception {

        ASMClassLoader classLoader = new ASMClassLoader(generateClass());
        Class<?> clazz = classLoader.loadClass("com.github.jvm.asm.CircleCalc");
        Object object = clazz.newInstance();
        Method method = clazz.getMethod("calcArea", double.class);
        Object rel = method.invoke(object, 10D);
        System.out.println(rel);
    }

}
