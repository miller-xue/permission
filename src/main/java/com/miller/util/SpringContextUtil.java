package com.miller.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by miller on 2018/7/22
 * spring applicationContext 工具
 * 获取Bean对象
 * @author miller-xue
 */
@Slf4j
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    /**
     * 从spring容器中获取对应class的对象
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T popBean(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception e) {
            log.error("获取Bean失败. class={} error={}",clazz , e);
            return null;
        }
    }

    /**
     * 从spring容器中取出对应name和class类型的对象
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T popBean(String name, Class<T> clazz) {
        try {
            return applicationContext.getBean(name, clazz);
        } catch (Exception e) {
            log.error("获取Bean失败. name={} class={} error={}",name,clazz , e);
            return null;
        }

    }
}
