package io.github.mjyoun.spring.utils;

import org.springframework.util.StopWatch;

import io.github.mjyoun.core.utils.TimeUtils;

/**
 * 실행 시간 기록, 표시를 위한 함수
 * 
 * @author MJ Youn
 * @since 2022. 12. 16.
 */
public class CustomStopWatch extends StopWatch {

    /**
     * time을 string 포맷에 맞게 출력
     * 
     * @return 포매팅된 time string
     * 
     * @author MJ Youn
     * @since 2022. 12. 16.
     */
    public String printLastTaskTimeNanos() {
        return TimeUtils.printPrettyNano(this.getLastTaskTimeNanos());
    }

}
