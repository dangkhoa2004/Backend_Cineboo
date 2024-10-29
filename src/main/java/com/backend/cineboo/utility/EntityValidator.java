package com.backend.cineboo.utility;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityValidator {

    /**
     * Lượt qua từng FieldError trong bindingResult.
     *
     * @param bindingResult
     * @return Map chứa các lỗi(key) và thông tin lỗi(value)
     */
    public static Map validateFields(BindingResult bindingResult) {
        List<FieldError> fe = bindingResult.getFieldErrors();
        if (!CollectionUtils.isEmpty(fe)) {
            Map errors = new HashMap();
            for (FieldError error : fe) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return errors;
        }
        return new HashMap();
    }
}
