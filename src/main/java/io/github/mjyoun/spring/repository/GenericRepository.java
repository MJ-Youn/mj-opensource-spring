package io.github.mjyoun.spring.repository;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import io.github.mjyoun.core.data.Result;
import io.github.mjyoun.spring.entity.GenericEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link JdbcTemplate} 사용을 위한 repository
 * 
 * @author MJ Youn
 * @since 2021. 12. 27.
 */
@Slf4j
public abstract class GenericRepository {

    /** Query를 properties로 부터 가져오기 위한 Message Source */
    private ReloadableResourceBundleMessageSource messageSource;
    /** JdbcTemplate */
    private JdbcTemplate jdbcTemplate;

    /**
     * @param jdbcTemplate
     *            JdbcTemplate
     * @param messageSource
     *            Query를 properties로 부터 가져오기 위한 Message Source
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    protected GenericRepository(JdbcTemplate jdbcTemplate, //
            ReloadableResourceBundleMessageSource messageSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.messageSource = messageSource;
    }

    /**
     * {@link MessageSource}에서 code에 해당하는 query를 가져오는 함수
     * 
     * @param code
     *            가져오고자 하는 code
     * @return code에 해당하는 query
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    protected String getQuery(String code) {
        return this.messageSource.getMessage(code, null, Locale.getDefault());
    }

    /**
     * query 질의 결과를 {@link GenericEntity} 타입의 목록 형태로 조회하는 함수
     * 
     * @param <T>
     *            GenericEntity를 상속받은 Entity
     * @param t
     *            GenericEntity를 상속받은 Entity 클래스
     * @param sql
     *            실행 query
     * @param args
     *            query 실행시 필요한 arguments
     * @return query 실행 결과
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    protected <T extends GenericEntity<T>> Result<List<T>> findAll(Class<T> t, String sql, Object... args) {
        List<T> resultData = null;

        if (args == null || args.length == 0) {
            resultData = this.jdbcTemplate.query(sql, this.getRowMapper(t));
        } else {
            resultData = this.jdbcTemplate.query(sql, this.getRowMapper(t), args);
        }

        return Result.ok(resultData);
    }

    /**
     * query 질의를 페이지네이션 정보와 함께 실행한 {@link GenericEntity} 타입의 목록 형태로 조회하는 함수
     * 
     * @param <T>
     *            GenericEntity를 상속받은 Entity
     * @param t
     *            GenericEntity를 상속받은 Entity 클래스
     * @param sql
     *            실행 query
     * @param pageable
     *            페이지네이션 정보
     * @param args
     *            query 실행시 필요한 arguments
     * @return 페이지네이션을 포함한 query 실행 결과
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    protected <T extends GenericEntity<T>> Result<List<T>> findAllWithPage(Class<T> t, String sql, Pageable pageable, Object... args) {
        StringBuffer sb = new StringBuffer() //
                .append(sql) //
                .append(this.createPagenationQuery(pageable));

        log.debug("Pageable: {}", this.createPagenationQuery(pageable));

        return this.findAll(t, sb.toString(), args);
    }

    /**
     * query 질의 결과를 하나의 {@link GenericEntity} 정보로 조회하는 함수
     * 
     * @param <T>
     *            GenericEntity를 상속받은 Entity
     * @param t
     *            GenericEntity를 상속받은 Entity 클래스
     * @param sql
     *            실행 query
     * @param args
     *            query 실행시 필요한 arguments
     * @return query 실행 결과
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    protected <T extends GenericEntity<T>> Result<T> findOne(Class<T> t, String sql, Object... args) {
        T resultData = null;

        try {
            if (args == null || args.length == 0) {
                resultData = this.jdbcTemplate.queryForObject(sql, this.getRowMapper(t));
            } else {
                resultData = this.jdbcTemplate.queryForObject(sql, this.getRowMapper(t), args);
            }
        } catch (EmptyResultDataAccessException e) { // 조회 된 결과가 없을 경우
        }

        return Result.ok(resultData);
    }

    /**
     * 특정 컬럼의 데이터 목록을 갖고, 해당 데이터에 해당하는 결과를 조회하는 함수
     * 
     * @param <T>
     *            GenericEntity를 상속받은 Entity
     * @param t
     *            GenericEntity를 상속받은 Entity 클래스
     * @param sql
     *            실행 query
     * @param dataColumn
     *            조회할 data column 이름. query 상에 ":dataColumn"으로 설정되어 있는 이름
     * @param datas
     *            조회할 data 목록
     * @return query 실행 결과
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    protected <T extends GenericEntity<T>> Result<List<T>> findAllInDatas(Class<T> t, String sql, String dataColumn, List<String> datas) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
        Map<String, List<String>> dataMap = new HashMap<>();
        dataMap.put(dataColumn, datas);

        return Result.ok(namedParameterJdbcTemplate.query(sql, dataMap, this.getRowMapper(t)));
    }

    /**
     * parameter map을 갖고 DB 조회를 요청하는 함수
     * 
     * @param <T>
     *            GenericEntity를 상속받은 Entity
     * @param t
     *            GenericEntity를 상속받은 Entity 클래스
     * @param sql
     *            실행 query
     * @param paramsMap
     *            query 조회 parameter map
     * @return query 실행 결과
     * 
     * @author MJ Youn
     * @since 2022. 01. 10.
     */
    protected <T extends GenericEntity<T>, P> Result<List<T>> findAllInDatas(Class<T> t, String sql, Map<String, List<P>> paramsMap) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());

        return Result.ok(namedParameterJdbcTemplate.query(sql, paramsMap, this.getRowMapper(t)));
    }

    /**
     * parameter map을 갖고 DB 조회를 요청하는 함수
     * 
     * @param <T>
     *            GenericEntity를 상속받은 Entity
     * @param t
     *            GenericEntity를 상속받은 Entity 클래스
     * @param sql
     *            실행 query
     * @param paramsMap
     *            query 조회 parameter map
     * @param pageable
     *            페이지네이션 정보
     * @return query 실행 결과
     * 
     * @author MJ Youn
     * @since 2022. 01. 10.
     */
    protected <T extends GenericEntity<T>, P> Result<List<T>> findAllInDatas(Class<T> t, String sql, Map<String, List<P>> paramsMap,
            Pageable pageable) {
        String queryWithPageable = new StringBuffer() //
                .append(sql) //
                .append(this.createPagenationQuery(pageable)) //
                .toString();

        log.debug("Pageable: {}", this.createPagenationQuery(pageable));

        return this.findAllInDatas(t, queryWithPageable, paramsMap);
    }

    /**
     * parameter map을 갖고 DB 조회 결과를 Integer로 받는 함수
     * 
     * @param <R>
     *            Return 타입
     * @param <P>
     *            Parameter 타입
     * @param sql
     *            실행 query
     * @param paramsMap
     *            query 조회 parameter map
     * @return query 실행 결과
     * 
     * @author MJ Youn
     * @since 2022. 01. 10.
     */
    protected <R, P> Result<R> executeWithParams(String sql, Map<String, List<P>> paramsMap, Class<R> returnType) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());

        return Result.ok(namedParameterJdbcTemplate.queryForObject(sql, paramsMap, returnType));
    }

    /**
     * query 실행 결과를 개수로 조회하는 함수
     * 
     * @param sql
     *            실행 query
     * @param args
     *            query 실행시 필요한 arguments
     * @return query 실행 결과
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    protected Result<Integer> execute(String sql, Object... args) {
        Integer resultData = null;

        if (args == null || args.length == 0) {
            resultData = this.jdbcTemplate.update(sql);
        } else {
            resultData = this.jdbcTemplate.update(sql, args);
        }

        return Result.ok(resultData);
    }

    /**
     * 여러 데이터를 하나의 query로 배치 실행하는 함수
     * 
     * @param sql
     *            실행 query
     * @param pss
     *            query 실행시 argument를 설정하기 위한 setter
     * @return query 실행 결과
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    protected Result<Integer> batch(String sql, BatchPreparedStatementSetter pss) {
        int[] resultDatas = this.jdbcTemplate.batchUpdate(sql, pss);
        Integer sum = Arrays.stream(resultDatas).sum();

        return Result.ok(sum);
    }

    /**
     * parameter log를 출력하기 위한 string을 생성하는 메소드
     * 
     * @param args
     *            arguments
     * @return arguments string
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    protected String createParameterLogString(Object... args) {
        StringBuffer sb = new StringBuffer();

        sb.append("PARAMETER:");

        for (Object arg : args) {
            sb.append(" ").append(arg);
        }

        return sb.toString();
    }

    /**
     * parameter log를 출력하기 위한 string을 생성하는 메소드
     * 
     * @param args
     *            arguments
     * @return arguments string
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    protected String createParameterLogString(String... args) {
        StringBuffer sb = new StringBuffer();

        sb.append("PARAMETER:");

        for (Object arg : args) {
            sb.append(" ").append(arg);
        }

        return sb.toString();
    }

    /**
     * Object List를 log로 출력하기 위한 string으로 생성하는 함수
     * 
     * @param args
     *            arguments
     * @return arguments string
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    protected String createListParameterString(List<?> args) {
        StringBuffer sb = new StringBuffer();

        sb.append("List PARAMETER: ");

        for (Object arg : args) {
            sb.append(" ").append(arg.toString());
        }

        return sb.toString();
    }

    /**
     * {@link GenericEntity}로부터 기본 설정한 {@link RowMapper}를 가져오는 함수
     * 
     * @param <T>
     *            GenericEntity를 상속받은 Entity
     * @param t
     *            GenericEntity를 상속받은 Entity 클래스
     * @return 기본 설정한 RowMapper 정보
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    private <T extends GenericEntity<T>> RowMapper<T> getRowMapper(Class<T> t) {
        RowMapper<T> rowMapper = null;

        try {
            rowMapper = t.getDeclaredConstructor().newInstance().getRowMapper();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            e.printStackTrace();
        }

        return rowMapper;
    }

    /**
     * {@link Pageable} 정보를 갖고 Query를 생성하는 함수
     * 
     * @param pageable
     *            Pageable 정보
     * @return 검색 조건 가장 뒤에 붙는 order, limit 정보
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    protected abstract String createPagenationQuery(Pageable pageable);

}
