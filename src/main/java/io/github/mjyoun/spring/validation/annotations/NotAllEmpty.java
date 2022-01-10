package io.github.mjyoun.spring.validation.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import io.github.mjyoun.spring.validation.NotAllEmptyValidator;

/**
 * 여러 개의 field 중에 하나 이상 값이 들어올 수 있도록 설정하기 위한 annotation
 * 
 * @author MJ Youn
 * @since 2022. 01. 10.
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { NotAllEmptyValidator.class })
public @interface NotAllEmpty {

    /**
     * 에러시 표시할 메시지 정보
     * 
     * @return 메시지
     */
    String message() default "옵션 값중 하나 이상의 값이 있어야 합니다.";

    /**
     * 그룹 이름 정보
     * 
     * @return 그룹 이름 목록
     */
    Class<?>[] groups() default {};

    /**
     * 오류 단계 정보
     * 
     * @return 오류 단계
     */
    Class<? extends Payload>[] payload() default {};

}
