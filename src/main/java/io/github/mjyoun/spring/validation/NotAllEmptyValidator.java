/**
 *
 */
package io.github.mjyoun.spring.validation;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import io.github.mjyoun.spring.validation.annotations.FieldGrouping;
import io.github.mjyoun.spring.validation.annotations.NotAllEmpty;
import lombok.extern.slf4j.Slf4j;

/**
 * 여러 개의 field 중에 하나 이상 값이 들어올 수 있도록 설정하기 위한 validator
 * 
 * @see NotAllEmpty
 * 
 * @author MJ Youn
 * @since 2022. 01. 10.
 */
@Slf4j
public class NotAllEmptyValidator implements ConstraintValidator<NotAllEmpty, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        /** 최종적으로 유효한지 확인하기 위한 flag */
        boolean validate = true;
        /** 그룹에 값이 있는 field가 있는지 확인하는 flag */
        Map<String, Boolean> hasDataInGroups = new HashMap<>();
        /** Class Annoataion 정보 */
        NotAllEmpty notAllEmptyAnnotation = value.getClass().getAnnotation(NotAllEmpty.class);

        Field[] fields = value.getClass().getDeclaredFields();

        for (Field field : fields) {
            FieldGrouping annotationInfo = field.getAnnotation(FieldGrouping.class);

            // not all empty annotation이 있는 Field에서만 실행
            if (annotationInfo != null) {
                // annotation에 설정되어 있는 그룹 목록
                String[] groupNames = annotationInfo.groupNames();

                // field data를 가져오기 위한 accessible 설정
                Boolean defaultAccessible = field.isAccessible();
                field.setAccessible(true);

                try {
                    // filed의 data
                    Object fieldValue = field.get(value);
                    // field의 data가 있는지 확인
                    Boolean dataHas = this.hasData(fieldValue);

                    // map에 데이터 설정
                    this.setHasDataInGroups(hasDataInGroups, groupNames, dataHas);

                    field.setAccessible(defaultAccessible);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    log.error("{}", e);
                }
            }
        }

        // 추가된 group 정보로 validate 확인
        for (Entry<String, Boolean> group : hasDataInGroups.entrySet()) {
            if (group.getValue() == false) {
                validate = false;
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(notAllEmptyAnnotation.message()) //
                        .addConstraintViolation();
                break;
            }
        }

        return validate;
    }

    /**
     * 데이터를 가지고 있는지 확인하는 함수
     * 
     * @param value
     *            empty인지 확인하기 위한 값
     * @return true면 null이 아니고 값이 있는 상태. false면 null이거나 empty 상태
     */
    private Boolean hasData(Object value) {
        if (value == null) {
            return false;
        } else {
            if (value instanceof Collection<?>) {
                return !((Collection<?>) value).isEmpty();
            } else if (value instanceof Object[]) {
                return ((Object[]) value).length != 0;
            } else if (value instanceof Map<?, ?>) {
                return !((Map<?, ?>) value).isEmpty();
            } else {
                return StringUtils.isNotBlank(value.toString());
            }
        }
    }

    /**
     * group map에 데이터 존재 여부를 설정하는 함수
     * 
     * @param groupMap
     *            설정할 group map 정보
     * @param groups
     *            추가할 group 이름 목록
     * @param hasData
     *            그룹의 데이터 존재 여부
     * 
     * @author MJ Youn
     * @since 2022. 01. 10.
     */
    private void setHasDataInGroups(@NotNull Map<String, Boolean> groupMap, @NotNull String[] groupNames, Boolean hasData) {
        // 그룹 이름 목록으로 맵을 돌면서 확인함
        for (String group : groupNames) {
            Boolean prevHasData = groupMap.get(group);

            // 이전에 들어 있는 값이 true일 경우 별도 작업을 하지 않음
            if (prevHasData != null && prevHasData == true) {
                continue;
            } else { // 그 이외의 경우 현재 값을 map에 추가함
                groupMap.put(group, hasData);
            }
        }
    }

}
