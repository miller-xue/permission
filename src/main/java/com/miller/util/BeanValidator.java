package com.miller.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.NonNull;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * Created by miller on 2018/7/21
 * 校验工具，对象属性校验
 */
public class BeanValidator {
    /**
     * validator工厂
     */
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

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

    public static Map<String, String> validateObject(Object first, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validateList(Lists.asList(first, objects));
        }else {
            return validate(first, new Class[0]);
        }
    }
}
