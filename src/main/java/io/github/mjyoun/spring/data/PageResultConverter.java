package io.github.mjyoun.spring.data;

import org.springframework.data.domain.Page;

import io.github.mjyoun.core.data.Result;

/**
 * {@link Page}를 담고 있는 {@link Result} 객체를 더 손쉽게 변환하기 위한 함수
 * 
 * @author MJ Youn
 * @since 2021. 12. 28.
 */
public class PageResultConverter<R extends Result<Page<T>>, T> {

    /** 결과 데이터를 담기 위한 객체 */
    private R result;

    /**
     * @param result
     *            {@link Page} 정보를 담고 있는 {@link Result} 객체
     */
    protected PageResultConverter(R result) {
        this.result = result;
    }

    /**
     * 실질적인 생성자
     * 
     * @param <T>
     *            {@link Page}에 포함되어 있는 객체
     * @param result
     *            {@link Page} 정보를 담고 있는 {@link Result} 객체
     * @return {@link PageResultConverter}
     * 
     * @author MJ Youn
     * @since 2021. 12. 28.
     */
    public static <T> PageResultConverter<Result<Page<T>>, T> of(Result<Page<T>> result) {
        return new PageResultConverter<>(result);
    }

    /**
     * converter에 포함되어 있는 데이터를 가져오기 위한 함수 
     * 
     * @return {@link Page} 정보를 담고 있는 {@link Result} 객체 
     * 
     * @author MJ Youn
     * @since 2021. 12. 28.
     */
    public R get() {
        return this.result;
    }

}
