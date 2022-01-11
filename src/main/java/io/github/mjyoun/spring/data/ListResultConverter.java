package io.github.mjyoun.spring.data;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import io.github.mjyoun.core.data.Result;

/**
 * {@link List}를 담고 있는 {@link Result} 객체를 더 손쉽게 변환하기 위한 함수
 * 
 * @author MJ Youn
 * @since 2021. 12. 28.
 */
public class ListResultConverter<R extends Result<List<T>>, T> {

    /** 결과 데이터를 담기 위한 객체 */
    private R result;

    /**
     * @param result
     *            {@link List} 정보를 담고 있는 {@link Result} 객체
     */
    protected ListResultConverter(R result) {
        this.result = result;
    }

    /**
     * 실질적인 생성자
     * 
     * @param <T>
     *            {@link List}에 포함되어 있는 객체
     * @param result
     *            {@link List} 정보를 담고 있는 {@link Result} 객체
     * @return {@link ListResultConverter}
     * 
     * @author MJ Youn
     * @since 2021. 12. 28.
     */
    public static <T> ListResultConverter<Result<List<T>>, T> of(Result<List<T>> result) {
        return new ListResultConverter<>(result);
    }

    /**
     * 정렬 함수. {@link Result}에 들어있는 {@link List}정보를 정렬 조건 {@link Comparator}에 맞춰 정렬한다.
     * 
     * @param comp
     *            비교 함수
     * @return 정렬된 결과 정보
     * 
     * @author MJ Youn
     * @since 2021. 12. 28.
     */
    public ListResultConverter<R, T> sort(@NotNull Comparator<T> comp) {
        if (this.result.isResult()) {
            if (this.result.getData() == null) {
                throw new NullPointerException("결과 데이터가 없습니다.");
            }

            this.result.getData().sort(comp);
        }

        return this;
    }

    /**
     * 데이터 모델 변경 함수. {@link Result}에 들어있는 {@link List}의 데이터 모델을 원하는 형태로 변경한다.
     * 
     * @param <D>
     *            변경하고자하는 데이터 모델 정보
     * @param d
     *            변경하고자하는 데이터 모델 클래스
     * @param mapper
     *            모델 변경 함수
     * @return 모델이 변경된 {@link Result} 객체
     * 
     * @author MJ Youn
     * @since 2021. 12. 28.
     */
    public <D> ListResultConverter<Result<List<D>>, D> map(@NotNull Class<D> d, @NotNull Function<T, D> mapper) {
        if (this.result.isResult()) {
            if (this.result.getData() == null) {
                throw new NullPointerException("결과 데이터가 없습니다.");
            }

            List<D> data = this.result.getData() //
                    .stream() //
                    .map(mapper) //
                    .collect(Collectors.toList());

            return new ListResultConverter<>(Result.ok(data));
        } else {
            return new ListResultConverter<>(Result.error(this.result));
        }
    }

    /**
     * 리스트 형태의 {@link ListResultConverter}를 페이지 형태인 {@link PageResultConverter}로 변환하는 함수.
     * 
     * @param pageable
     *            페이지 정보
     * @param totalCntFunc
     *            전체 페이지 개수를 계산하기 위한 함수
     * @return {@link PageResultConverter}
     * 
     * @author MJ Youn
     * @since 2021. 12. 28.
     */
    public PageResultConverter<Result<Page<T>>, T> toPage(@NotNull Pageable pageable, @NotNull Supplier<Long> totalCntFunc) {
        if (this.result.isResult()) {
            if (this.result.getData() == null) {
                throw new NullPointerException("결과 데이터가 없습니다.");
            }

            Page<T> page = new PageImpl<>(this.result.getData(), pageable, totalCntFunc.get());
            return PageResultConverter.of(Result.ok(page));
        } else {
            return PageResultConverter.of(Result.error(this.result));
        }
    }

    /**
     * 리스트 형태의 {@link ListResultConverter}를 페이지 형태인 {@link PageResultConverter}로 변환하는 함수.
     * 
     * @param pageable
     *            페이지 정보
     * @param totalCnt
     *            전체 페이지 개수
     * @return {@link PageResultConverter}
     * 
     * @author MJ Youn
     * @since 2022. 01. 11.
     */
    public PageResultConverter<Result<Page<T>>, T> toPage(@NotNull Pageable pageable, Long totalCnt) {
        if (this.result.isResult()) {
            if (this.result.getData() == null) {
                throw new NullPointerException("결과 데이터가 없습니다.");
            }

            Page<T> page = new PageImpl<>(this.result.getData(), pageable, totalCnt);
            return PageResultConverter.of(Result.ok(page));
        } else {
            return PageResultConverter.of(Result.error(this.result));
        }
    }

    /**
     * converter에 포함되어 있는 데이터를 가져오기 위한 함수
     * 
     * @return {@link List} 정보를 담고 있는 {@link Result} 객체
     * 
     * @author MJ Youn
     * @since 2021. 12. 28.
     */
    public R get() {
        return this.result;
    }

}
