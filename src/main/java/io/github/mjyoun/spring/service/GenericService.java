package io.github.mjyoun.spring.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 서비스 로직에서 사용하는 공통 기능들을 정리한 서비스
 * 
 * @author MJ Youn
 * @since 2021. 12. 28.
 */
public class GenericService {

    /** model 변경을 위한 Object Mapper */
    protected ObjectMapper objectMapper;

    /**
     * @param objectMapper
     *            {@link ObjectMapper}
     */
    protected GenericService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * list object를 변경하는 함수
     * 
     * @param <S>
     *            list의 source object type
     * @param <D>
     *            destination object type
     * @param src
     *            source list
     * @param destClass
     *            destination class
     * @return destination object list
     * 
     * @author MJ Youn
     * @since 2021. 12. 28.
     */
    protected <S, D> List<D> convertList(@NotNull List<S> src, @NotNull Class<D> destClass) {
        if (src == null) {
            return null;
        } else {
            return src //
                    .stream() //
                    .map(s -> this.objectMapper.convertValue(s, destClass)) //
                    .collect(Collectors.toList());
        }
    }

}
