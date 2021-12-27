package io.github.mjyoun.spring.entity;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * {@link JdbcTemplate} 사용을 위한 Entity 선언 
 * 
 * @author MJ Youn
 * @since 2021. 12. 27.
 */
public interface GenericEntity<T> {
    
    /**
     * find 함수에서 사용할 기본 rowMapper
     * 
     * @return 기본 RowMapper
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    public RowMapper<T> getRowMapper();

}
