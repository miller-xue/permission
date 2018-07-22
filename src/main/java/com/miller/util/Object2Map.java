package com.miller.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by miller on 2018/7/21
 */
@Slf4j
public class Object2Map {

    //TODO 用get属性名取值，不要强行取值
    public static Map<String,Object> object2Map(Object object){
        Map<String, Object> result = new HashMap<String, Object>();
        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(object);
            } catch (IllegalAccessException e) {
                log.error("Object 2 Map exception name:{}, error:{}:" + field.getName(),e);
                e.printStackTrace();
            }

            if (value == null) {
                continue;
            }
            result.put(field.getName(), value);
        }
        return result;

    }
}
