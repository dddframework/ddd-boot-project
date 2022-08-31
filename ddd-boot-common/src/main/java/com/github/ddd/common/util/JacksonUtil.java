package com.github.ddd.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON 工具
 *
 * @author ranger
 */
public class JacksonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // 忽略未知的JSON字段
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 不允许基本类型为null
        MAPPER.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
    }

    /**
     * 获取ObjectMapper
     *
     * @return ObjectMapper
     */
    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    /**
     * 转换集合对象
     *
     * @param jsonArray     String
     * @param typeReference TypeReference
     * @param <T>           T
     * @return T
     */
    public static <T> T toCollection(String jsonArray, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(jsonArray, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json convert error", e);
        }
    }

    /**
     * 转换普通对象
     *
     * @param jsonString String
     * @param beanClass  Class
     * @param <T>        T
     * @return T
     */
    public static <T> T toBean(String jsonString, Class<T> beanClass) {
        try {
            return MAPPER.readValue(jsonString, beanClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json convert error", e);
        }
    }

    /**
     * 转换JSON字符串
     *
     * @param obj Object
     * @return String
     */
    public static String toJsonStr(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json convert error", e);
        }
    }

    /**
     * 格式化转换JSON字符串
     *
     * @param obj Object
     * @return String
     */
    public static String toPrettyJsonStr(Object obj) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json convert error", e);
        }
    }
}
