package com.github.jvm.io;

import org.junit.Test;

import java.nio.ByteBuffer;

public class ByteBufferTest {

    /*
        ByteBuffer:
        mark: 标记, 默认 -1
        capacity： 容量, 初始化给
        position:  读写位置, 默认0
        limit: 读写边界, 初始值与capacity相等


     */
    @Test
    public void test(){

        byte[] bytes = "test".getBytes();

        byte[] dest = new byte[bytes.length];

        // 创建一个ByteBuffer => mark = -1, capacity = limit = 1024, position = 0
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 写入数据，每写一个byte, position + 1
        buffer.put(bytes);

        // 写读转换 => limit = position, position = 0
        buffer.flip();

        // 位置标记 => mark = position
        buffer.mark();

        if (buffer.hasRemaining()){
            // 读取数据，每读一个byte, position + 1
            buffer.get(dest);
            System.out.println(new String(dest));

            // 标记mark复位， position = mark
            buffer.reset();

            // 读取数据，每读一个byte, position + 1
            buffer.get(dest);
            System.out.println(new String(dest));
        }

        // 清除数据 => buffer数据内容不变，mark, capacity, position, limit 复位
        buffer.clear();

        buffer.put(bytes);

        buffer.flip();

        if (buffer.hasRemaining()){
            buffer.get(dest);
            System.out.println(new String(dest));
        }
    }
}
