package com.permissions.utils;

import org.springframework.beans.BeanUtils;

public class Converter {
    public static <E, D> D convertToDTO(E entity, Class<D> dtoClass) {
        if (entity == null) {
            return null;
        }

        try {
            D dtoInstance = dtoClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(entity, dtoInstance);
            return dtoInstance;
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir la entidad al DTO", e);
        }
    }
}