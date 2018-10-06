package com.cn.pwd.util;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import java.util.Base64;

public class SerializerUtil{

    public static <T>String serialize(Class<T> tClass,T obj){
        RuntimeSchema<T> schema = RuntimeSchema.createFrom(tClass);
        byte[] bytes = ProtobufIOUtil.toByteArray(obj,schema,
                LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static <T>T deserialize(Class<T> tClass,String str){
        RuntimeSchema<T> schema = RuntimeSchema.createFrom(tClass);
        T t = schema.newMessage();
        ProtobufIOUtil.mergeFrom(Base64.getDecoder().decode(str),t,schema);
        return t;
    }


}
