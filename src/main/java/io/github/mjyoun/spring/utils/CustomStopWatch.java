package io.github.mjyoun.spring.utils;

import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;

import io.github.mjyoun.core.utils.TimeUtils;

/**
 * 실행 시간 기록, 표시를 위한 함수
 * 
 * @author MJ Youn
 * @since 2022. 12. 16.
 */
public class CustomStopWatch extends StopWatch {

    private String timeFormat = "hh:MM:ss.SSS uuuu nnnn";

    /**
     * (non-javadoc)
     * 
     * @author MJ Youn
     * @since 2024. 05. 09.
     */
    public CustomStopWatch() {
        super();
    }

    /**
     * (non-javadoc)
     * 
     * @param id
     *            stopwatch 이름. print시 사용됨
     * 
     * @author MJ Youn
     * @since 2024. 05. 09.
     */
    public CustomStopWatch(String id) {
        super(id);
    }

    /**
     * (non-javadoc)
     * 
     * @param id
     *            stopwatch 이름. print시 사용됨
     * @param timeFormat
     *            시간 출력 포맷
     * 
     * @author MJ Youn
     * @since 2024. 05. 09.
     */
    public CustomStopWatch(String id, String timeFormat) {
        super(id);

        this.timeFormat = timeFormat;
    }

    /**
     * 타임 포맷 설정
     * 
     * @param timeFormat
     *            타임 포맷. {@link TimeUtils}
     * 
     * @author MJ Youn
     * @since 2024. 05. 09.
     */
    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    /**
     * time을 string 포맷에 맞게 출력
     * 
     * @return 포매팅된 time string
     * 
     * @author MJ Youn
     * @since 2022. 12. 16.
     */
    public String printLastTaskTimeNanos() {
        return this.printNano(this.getLastTaskTimeNanos());
    }

    /**
     * @see StopWatch#shortSummary()
     * 
     * @author MJ Youn
     * @since 2024. 05. 09.
     */
    @Override
    public String shortSummary() {
        return "'" + getId() + "'의 소요 시간: " + this.printNano(getTotalTimeNanos());
    }

    /**
     * @see StopWatch#prettyPrint()
     * 
     * @author MJ Youn
     * @since 2024. 05. 09.
     */
    @Override
    public String prettyPrint() {
        StringBuilder sb = new StringBuilder("\n" + shortSummary());

        NumberFormat pf = NumberFormat.getPercentInstance();
        pf.setMinimumIntegerDigits(3);
        pf.setGroupingUsed(false);

        sb.append('\n');
        sb.append("--------------------");
        sb.append(StringUtils.repeat("-", this.timeFormat.length() - 3));
        sb.append("\n");
        sb.append("time");
        sb.append(StringUtils.repeat(" ", this.timeFormat.length() - 3));
        sb.append(" %     Task name\n");
        sb.append("--------------------");
        sb.append(StringUtils.repeat("-", this.timeFormat.length() - 3));
        sb.append("\n");

        for (TaskInfo task : getTaskInfo()) {
            sb.append(this.printNano(task.getTimeNanos())).append("  ");
            sb.append(pf.format((double) task.getTimeNanos() / getTotalTimeNanos())).append("  ");
            sb.append(task.getTaskName()).append('\n');
        }

        return sb.toString();
    }

    /**
     * nano second 출력
     * 
     * @param nano
     *            nano second
     * @return nano second를 보기 좋게 출력
     * 
     * @author MJ Youn
     * @since 2024. 05. 09.
     */
    private String printNano(long nano) {
        return TimeUtils.printPrettyNano(nano, TimeUnit.HOURS, this.timeFormat);
    }

}
