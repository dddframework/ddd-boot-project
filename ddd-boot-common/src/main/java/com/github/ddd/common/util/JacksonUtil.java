package com.github.ddd.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ddd.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 * JSON 工具
 *
 * @author ranger
 */
@Slf4j
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
    public static <T> T toType(String jsonArray, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(jsonArray, typeReference);
        } catch (JsonProcessingException e) {
            log.error("json 转换失败 原字符串是 {}", jsonArray);
            throw new SystemException("json convert error", e);
        }
    }

    /**
     * 转换List对象
     *
     * @param jsonArray String
     * @param <T>       T
     * @return T
     */
    public static <T> List<T> toList(String jsonArray, Class<T> tClass) {
        return toType(jsonArray, new TypeReference<List<T>>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        });
    }

    /**
     * 转换Set对象
     *
     * @param jsonArray String
     * @param <T>       T
     * @return T
     */
    public static <T> Set<T> toSet(String jsonArray, Class<T> tClass) {
        return toType(jsonArray, new TypeReference<Set<T>>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        });
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
            log.error("json 转换失败 原字符串是 {}", jsonString);
            throw new SystemException("json convert error", e);
        }
    }

    /**
     * 转换JSON字符串
     *
     * @param obj Object
     * @return String
     */
    public static String toJsonStr(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new SystemException("json convert error", e);
        }
    }

    /**
     * 格式化转换JSON字符串
     *
     * @param obj Object
     * @return String
     */
    public static String toPrettyJsonStr(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new SystemException("json convert error", e);
        }
    }

}
