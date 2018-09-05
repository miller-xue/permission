package com.miller.util;

import com.google.common.collect.Maps;
import com.miller.common.RequestHolder;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by miller on 2018/7/23
 * @author Miller
 * 系统工具类,暂时未分类,先放到一个工具类中
 * //TODO 工具分类
 */
public class SysUtil {

    /**
     * Object 2Nap
     * @param object
     * @return
     */
    public static Map<String, Object> object2Map(Object object) {
        if (object == null) {
            return Maps.newHashMap();
        }
        Field[] declaredFields = object.getClass().getDeclaredFields();
        Map<String, Object> result = new HashMap<String, Object>(declaredFields.length);
        for (Field field : declaredFields) {
            String attrName = field.getName();
            Object value = ConvertUtil.invokeGetMethod(object, attrName);
            if (value == null) {
                continue;
            }
            result.put(attrName, value);
        }
        return result;
    }


    /**
     * 空返回空,首字母大写
     * @param s
     * @return
     */
    public static String toUpperCaseFirstOne(String s) {
        if (StringUtils.isBlank(s)) {
            return "";
        }
        char[] chars = s.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char)(chars[0] - 32);
        }
        return new String(chars);
    }

    public static boolean isAjax(HttpServletRequest request) {
        String xReq = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With")
    }

    public static void invokeSetOperate(Object object) {
        ConvertUtil.invokeSetMethod(object, "operator", RequestHolder.getCurrentUser().getUsername());
        ConvertUtil.invokeSetMethod(object, "operateIp", IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        ConvertUtil.invokeSetMethod(object, "operateTime", new Date());
    }
}
