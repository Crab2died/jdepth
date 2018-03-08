package com.github.jvm;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Kryo序列化框架测试
 * Kryo不是线程安全的，所以在多线程使用时要注意
 *
 * @author : Crab2Died
 * 2018/03/08  16:19:06
 */
public class KryoSerTest {

    private Kryo kryo;

    @Before
    public void init() {
        kryo = new Kryo();
        // 支持对象循环引用（否则会栈溢出）
        kryo.setReferences(true);
        //不强制要求注册类（注册行为无法保证多个 JVM 内同一个类的注册编号相同；而且业务系统中大量的 Class 也难以一一注册）
        kryo.setRegistrationRequired(false);
    }

    @Test
    public void test1() {
        Output output = new Output(1024);
        kryo.writeObject(output, new int[]{1, 2, 3, 4, 5});
        output.flush();
        output.close();
        System.out.println(output.total());

        Input input = new Input(output.toBytes());
        int[] arr = kryo.readObject(input, int[].class);
        System.out.println(Arrays.toString(arr));
        input.close();
    }

}
