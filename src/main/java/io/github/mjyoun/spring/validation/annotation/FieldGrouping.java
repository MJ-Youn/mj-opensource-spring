package io.github.mjyoun.spring.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * field를 grouping하기 위한 annotation. <br>
 * 그 이외의 별다른 설정은 없음.
 * 
 * @author MJ Youn
 * @since 2022. 01. 10.
 */
@Documented
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldGrouping {

    /**
     * 그룹핑 정보
     * 
     * @return 그룹 목록
     */
    String[] groupNames() default { "default" };

}
