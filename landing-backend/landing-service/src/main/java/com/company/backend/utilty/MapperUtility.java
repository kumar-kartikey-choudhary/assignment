package com.company.backend.utilty;

import com.company.backend.client.dto.ClientDto;
import com.company.backend.client.model.Client;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;

public class MapperUtility {

    public static <S, T> T sourceToTarget(S source , Class<T> targetClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T target = targetClass.getDeclaredConstructor().newInstance();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static <S, T> T sourceToTarget(S source, Class<T> targetClass, String... ignoredFields)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        T target = targetClass.getDeclaredConstructor().newInstance();
        BeanUtils.copyProperties(source, target, ignoredFields);
        return target;
    }

}
