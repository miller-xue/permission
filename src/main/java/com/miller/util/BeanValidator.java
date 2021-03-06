package com.miller.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.miller.Exception.ParamException;
import com.miller.enums.result.SysResult;
import lombok.NonNull;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * Created by miller on 2018/7/21
 * 校验工具，对象属性校验
 * @author Miller
 */
public class BeanValidator {
    /**
     * validator工厂
     */
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * 验证单个类
     * @param t
     * @param groups
     * @param <T>
     * @return
     */
    private static <T> Map<String, String> validate(T t, Class... groups) {
        Validator validator = validatorFactory.getValidator();

        Set validateResult = validator.validate(t, groups);
        // 校验无值
        if (validateResult.isEmpty()) {
            return Collections.emptyMap();
        }else {
            LinkedHashMap errors = Maps.newLinkedHashMap();
            Iterator iterator = validateResult.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation violation = (ConstraintViolation) iterator.next();
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errors;
        }
    }

    /**
     * 校验一个对象集合,出现校验未通过直接返回当前对象的校验,否返回空
     * @param collection
     * @return
     */
    public static Map<String, String> validateList(@NonNull Collection<?> collection) {
        Iterator<?> iterator = collection.iterator();
        Map errors;
        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object, new Class[0]);
        } while (errors.isEmpty());
        return errors;
    }

    /**
     * TODO
     * @param first
     * @param objects
     * @return
     */
    public static Map<String, String> validateObject(Object first, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validateList(Lists.asList(first, objects));
        }else {
            return validate(first, new Class[0]);
        }
    }

    /**
     * 校验单个对象,校验失败会抛出异常
     * @param param
     * @throws ParamException
     */
    public static void check(Object param)throws ParamException{
        Map<String, String> map = validateObject(param);
        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(SysResult.PARAM_ERROR, map);
        }
    }
}
