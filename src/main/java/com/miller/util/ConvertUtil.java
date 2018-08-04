package com.miller.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by miller on 2018/7/22
 * @author Miller
 * 反射工具类
 */
@Slf4j
public class ConvertUtil {

    /**
     * 执行指定目标对象指定属性的set方法
     * @param target 目标对象
     * @param attributeName 属性名
     * @param args 设置的值
     * @return 无
     */
    public static void invokeSetMethod(Object target, String attributeName, Object... args) {
        if (target == null || StringUtils.isBlank(attributeName)) {
            throw new NullPointerException("目标类,或属性名不能为空");
        }
        String methodName = new StringBuffer("set").append(SysUtil.toUpperCaseFirstOne(attributeName)).toString();
        try {
            Method method = getDeclaredMethod(target, methodName, getDeclaredField(target, attributeName).getType());
            method.invoke(target, args);
        } catch (Exception e) {
            log.error("invokeSetMethod error target:{}, attributeName:{}, args:[] error:{}", JsonMapper.obj2String(target), attributeName, JsonMapper.obj2String(args), e);
        }
        return;
    }



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
        String methodName = new StringBuffer("get").append(SysUtil.toUpperCaseFirstOne(attributeName)).toString();
        try {
            Method method = getDeclaredMethod(target,methodName);
            return method.invoke(target);
        } catch (Exception e) {
            log.error("invokeGetMethod error, target:{} attributeName:{} error:{} ", JsonMapper.obj2String(target), attributeName, e);
            return null;
        }
    }

    /**
     * 循环向上转型, 获取对象的 指定方法的Field
     * @param target : 子类对象
     * @param fieldName : 子父类中属性名
     * @return 父类中的方法对象
     */
    private static Field getDeclaredField(Object target, String fieldName) {
        if (target == null || StringUtils.isBlank(fieldName)) {
            throw new NullPointerException("target or fieldName 不能为空");
        }
        for(Class<?> clazz = target.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                continue;
            }
        }
        return null;
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
