package io.github.mjyoun.spring.data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
     * Result가 아닌 page 생성자
     * 
     * @param <T>
     *            {@link Page}에 포함되어 있는 객체
     * @param page
     *            {@link Page}
     * @return 항상 true인, {@link PageResultConverter}
     * 
     * @author MJ Youn
     * @since 2022. 03. 15.
     */
    public static <T> PageResultConverter<Result<Page<T>>, T> of(Page<T> page) {
        return new PageResultConverter<>(Result.ok(page));
    }

    /**
     * 데이터 모델 변경 함수. {@link Result}에 들어있는 {@link Page}의 데이터 모델을 원하는 형태로 변경한다.
     * 
     * @param <D>
     *            변경하려는 데이터 모델 정보
     * @param d
     *            변경하려는 데이터 모델 클래스
     * @param mapper
     *            모델 변경 함수
     * @return 모데리 변경된 {@link PageResultConverter} 객체
     * 
     * @author MJ Youn
     * @since 2022. 03. 15.
     */
    public <D> PageResultConverter<Result<Page<D>>, D> map(@NotNull Class<D> d, @NotNull Function<T, D> mapper) {
        if (this.result.isResult()) {
            if (this.result.getData() == null) {
                throw new NullPointerException("결과 데이터가 없습니다.");
            }

            List<D> data = this.result.getData() //
                    .get() //
                    .map(mapper) //
                    .collect(Collectors.toList());
            Pageable pageable = this.result.getData().getPageable();

            return new PageResultConverter<>(Result.ok(new PageImpl<>(data, pageable, this.result.getData().getTotalElements())));
        } else {
            return new PageResultConverter<>(Result.error(this.result));
        }
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
