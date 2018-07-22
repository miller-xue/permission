package com.miller.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * Created by miller on 2018/7/22
 * 反射工具类
 */
@Slf4j
public class ConvertUtil {

    /**
     * 指定目标对象指定属性的get方法,并返回值
     * @param target 目标对象
     * @param attributeName 属性名
     * @return 值
     */
    public static Object invokeGetMethod(Object target, String attributeName) {
        if (target == null || StringUtils.isBlank(attributeName)) {
            return null;
        }
        String methodName = "get" + (attributeName.toCharArray()[0] + "").toUpperCase() + attributeName.substring(1);
        try {
            Method method = getDeclaredMethod(target,methodName);
            return method.invoke(target);
        } catch (Exception e) {
            log.error("invokeGetMethod error, target:{} attributeName:{} error:{} ", JsonMapper.obj2String(target), attributeName, e);
            return null;
        }
    }


    /**
     * 循环向上转型, 获取对象的 指定方法的Method
     * @param target : 子类对象
     * @param methodName : 子父类中的方法名
     * @param parameterTypes : 子父类中的方法参数类型
     * @return 父类中的方法对象
     */
    private static Method getDeclaredMethod(Object target, String methodName, Class<?> ... parameterTypes){
        if (target == null || StringUtils.isBlank(methodName)) {
            throw new NullPointerException("target or methodName 不能为空");
        }
        for(Class<?> clazz = target.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                return clazz.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) { // 当前对象没有找到该属性
                continue;
            }
        }
        return null;
    }
}
