package com.github.io.protocol.c2d.codc.protostuff;

import com.github.io.protocol.c2d.message.C2DHeader;
import com.github.io.protocol.c2d.message.C2DMessage;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Protostuff序列化/反序列化
 * 并对{@link Schema}对象缓存于{@link ProtostuffCodec#CACHE_SCHEMA}内
 *
 * @author : Crab2Died
 * 2017/12/15  09:46:36
 */
@SuppressWarnings("all")
public class ProtostuffCodec {

    private static final Logger logger = LoggerFactory.getLogger(ProtostuffCodec.class);

    private static final Map<Class<?>, Schema<?>> CACHE_SCHEMA = new ConcurrentHashMap<>();

    private static Objenesis beanFactory = new ObjenesisStd(true);

    public static <T> byte[] encode(T obj) {

        if (null == obj)
            return null;
        Schema<T> schema;
        Class<T> clazz = (Class<T>) obj.getClass();
        if (null != CACHE_SCHEMA.get(obj.getClass())) {
            schema = (Schema<T>) CACHE_SCHEMA.get(clazz);
            logger.debug("Loaded [" + clazz.getName() + "] schema from cache");
        } else {
            schema = RuntimeSchema.getSchema(clazz);
            CACHE_SCHEMA.put(clazz, schema);
            logger.debug("Added [" + clazz.getName() + "] schema into cache");
        }
        if (null == schema)
            return null;
        return ProtostuffIOUtil.toByteArray(obj, schema, LinkedBuffer.allocate(256));
    }

    public static <T> T decode(byte[] bytes, Class<T> clazz)
            throws IllegalAccessException, InstantiationException {

        Schema<T> schema;
        if (null != CACHE_SCHEMA.get(clazz)) {
            schema = (Schema<T>) CACHE_SCHEMA.get(clazz);
            logger.debug("Loaded [" + clazz.getName() + "] schema from cache");
        } else {
            schema = RuntimeSchema.getSchema(clazz);
            CACHE_SCHEMA.put(clazz, schema);
            logger.debug("Added [" + clazz.getName() + "] schema into cache");
        }
        if (null == schema)
            return null;
        T obj = beanFactory.newInstance(clazz);
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }


    public static void main(String... args) {

        C2DMessage message = new C2DMessage();
        C2DHeader header = new C2DHeader();
        header.setMagic(323435);
        header.setSerial(45678L);
        Map<String, Object> map = new HashMap<>();
        header.setAttachment(map);
        map.put("a", new Object());
        map.put("b", "a");
        message.setBody(header);
        message.setHeader(header);

        try {
            System.out.println("原对象为：" + message);
            byte[] bytes = encode(message);
            System.out.println("序列化后byte数组长度为：" + bytes.length);
            C2DMessage _d = null;
            _d = decode(bytes, C2DMessage.class);
            System.out.println("反序列化后对象为：" + _d);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

    }
}
