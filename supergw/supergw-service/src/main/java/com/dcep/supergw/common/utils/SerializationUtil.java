package com.dcep.supergw.common.utils;

public interface SerializationUtil<T> {

    /**
     * 序列化---将对象序列化成字符串
     */
    String serialization(T o);

    /**
     * 反序列化---将字符串发序列化成对象
     */
    T deserialization(String context, Class<?> clazz);


    /**
     * 序列化-----将对象序列化成byte[]
     */
    byte[] serialize(T o);

    /**
     * 反序列化----将byte[] 反序列化成对象
     */
    T deserialize(byte[] content, Class<?> clazz);
}
