package io.github.mjyoun.spring.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * {@link JdbcTemplate} 관련된 유틸 클래스
 * 
 * @author MJ Youn
 * @since 2021. 12. 27.
 */
public class JdbcUtils {

    /**
     * nullable한 data를 설정하는 함수
     * 
     * @param <T>
     *            data type
     * @param ps
     *            {@link PreparedStatement}
     * @param index
     *            ps에 data를 넣기 위한 index. 1부터 시작
     * @param data
     *            nullable한 data
     * @param type
     *            data type
     * @throws SQLException
     *             if parameterIndex does not correspond to a parameter marker in the SQL statement; if a database access error occurs or this method
     *             is called on a closed <code>PreparedStatement</code>.
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    public static <T> void setPreparedStatement(@NotNull PreparedStatement ps, @Min(1) int index, T data, Class<T> type) throws SQLException {
        if (data == null) {
            ps.setNull(index, Types.NULL);
        } else {
            if (type == Integer.class) {
                ps.setInt(index, (Integer) data);
            } else if (type == Long.class) {
                ps.setLong(index, (Long) data);
            } else if (type == Double.class) {
                ps.setDouble(index, (Double) data);
            } else if (type == Float.class) {
                ps.setFloat(index, (Float) data);
            } else {
                ps.setString(index, data.toString());
            }
        }
    }

    /**
     * {@link ResultSet}에서 String을 가져오는 함수. 값이 없을 경우 null을 출력한다.
     *
     * @param rs
     *            {@link ResultSet}
     * @param columnName
     *            조회할 컬럼 이름
     * @return 컬럼에 해당하는 값. 존재하지 않는 컬럼일 경우 Null
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    public static String getString(@NotNull ResultSet rs, String columnName) {
        try {
            return rs.getString(columnName);
        } catch (SQLException ignore) {
            return null;
        }
    }

    /**
     * {@link ResultSet}에서 nullable한 {@link Long}을 가져오는 함수
     *
     * @param rs
     *            {@link ResultSet}
     * @param columnName
     *            조회할 컬럼 이름
     * @return 컬럼에 해당하는 값. 존재하지 않는 컬럼일 경우 Null
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    public static Long getLong(@NotNull ResultSet rs, String columnName) {
        try {
            long data = rs.getLong(columnName);
            return rs.wasNull() ? null : data;
        } catch (SQLException ignore) {
            return null;
        }
    }

    /**
     * {@link ResultSet}에서 nullable한 {@link Integer}를 가져오는 함수
     *
     * @param rs
     *            {@link ResultSet}
     * @param columnName
     *            조회할 컬럼 이름
     * @return 컬럼에 해당하는 값. 존재하지 않는 컬럼일 경우 Null
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    public static Integer getInteger(@NotNull ResultSet rs, String columnName) {
        try {
            int data = rs.getInt(columnName);
            return rs.wasNull() ? null : data;
        } catch (SQLException ignore) {
            return null;
        }
    }

    /**
     * {@link ResultSet}에서 nullable한 {@link Boolean}을 가져오는 함수
     *
     * @param rs
     *            {@link ResultSet}
     * @param columnName
     *            조회할 컬럼 이름
     * @return 컬럼에 해당하는 값. 존재하지 않는 컬럼일 경우 Null
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    public static Boolean getBoolean(@NotNull ResultSet rs, String columnName) {
        try {
            boolean data = rs.getBoolean(columnName);
            return rs.wasNull() ? null : data;
        } catch (SQLException ignore) {
            return null;
        }
    }

    /**
     * {@link ResultSet}에서 nullable한 {@link LocalDateTime}을 가져오는 함수
     *
     * @param rs
     *            {@link ResultSet}
     * @param columnName
     *            조회할 컬럼 이름
     * @return 컬럼에 해당하는 값. 존재하지 않는 컬럼일 경우 Null
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    public static LocalDateTime getLocalDateTime(@NotNull ResultSet rs, String columnName) {
        try {
            Timestamp ts = rs.getTimestamp(columnName);
            return rs.wasNull() ? null : ts.toLocalDateTime();
        } catch (SQLException ignore) {
            return null;
        }
    }

    /**
     * {@link ResultSet}에서 nullable한 {@link Float}을 가져오는 함수
     *
     * @param rs
     *            {@link ResultSet}
     * @param columnName
     *            조회할 컬럼 이름
     * @return 컬럼에 해당하는 값. 존재하지 않는 컬럼일 경우 Null
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    public static Float getFloat(@NotNull ResultSet rs, String columnName) {
        try {
            float data = rs.getFloat(columnName);
            return data;
        } catch (SQLException ignore) {
            return null;
        }
    }

    /**
     * 특정 텍스트로 시작하는 컬럼 목록을 조회하는 함수
     * 
     * @param rs
     *            {@link ResultSet}
     * @param startsWith
     *            시작하는 문자열. 대/소문자 구분하지 않음
     * @return 컬럼/값 맵핑 정보
     * @throws SQLException
     *             if a database access error occurs or this method is called on a closed result set
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    public static Map<String, Object> getColumnsStartsWith(@NotNull ResultSet rs, @NotNull String startsWith) throws SQLException {
        Map<String, Object> datas = new HashMap<>();
        ResultSetMetaData meta = rs.getMetaData();
        // ResultSet 결과로 조회된 컬럼 개수
        int columnCount = meta.getColumnCount();

        for (int i = 0; i < columnCount; i++) {
            // 조회된 column 이름
            String columnName = meta.getColumnName(i + 1);

            // 특정 문자열로 시작하는 column을 모음
            if (StringUtils.startsWithIgnoreCase(columnName, startsWith)) {
                datas.put(columnName, JdbcUtils.getString(rs, columnName));
            }
        }

        return datas;
    }

    /**
     * 여러 개의 쿼리로 inner join한 쿼리를 생성하는 함수
     * 
     * @param queries
     *            쿼리 목록
     * @return inner join한 쿼리
     * 
     * @author MJ Youn
     * @since 2022. 01. 19.
     */
    public static String innerJoinQuery(List<String> queries) {
        String query = null;

        if (queries == null || queries.size() < 1) {
            throw new IllegalArgumentException("하나 이상의 query가 있어야 join을 할 수 있습니다.");
        } else if (queries.size() == 1) {
            query = queries.get(0);
        } else {
            StringBuilder sb = new StringBuilder() //
                    .append("SELECT t_0.* FROM (") //
                    .append("(").append(queries.get(0)).append(") t_0");

            for (int i = 1; i < queries.size(); i++) {
                sb.append(" INNER JOIN (").append(queries.get(i)).append(") t_").append(i) //
                        .append(" ON t_").append(i - 1).append(".id = t_").append(i).append(".id");
            }

            sb.append(")");
            query = sb.toString();
        }

        return query;
    }

    /**
     * 여러 개의 쿼리로 inner join하여 개수를 세는 쿼리를 생성하는 함수
     * 
     * @param queries
     *            쿼리 목록
     * @return inner join한 쿼리
     * 
     * @author MJ Youn
     * @since 2022. 01. 19.
     */
    public static String innerJoinCountQuery(List<String> queries) {
        String query = null;

        if (queries == null || queries.size() < 1) {
            throw new IllegalArgumentException("하나 이상의 query가 있어야 join을 할 수 있습니다.");
        } else if (queries.size() == 1) {
            query = new StringBuilder("SELECT COUNT(*) FROM (") //
                    .append(queries.get(0)) //
                    .append(") t_0") //
                    .toString();
        } else {
            StringBuilder sb = new StringBuilder() //
                    .append("SELECT COUNT(t_0.*) FROM (") //
                    .append("(").append(queries.get(0)).append(") t_0");

            for (int i = 1; i < queries.size(); i++) {
                sb.append(" INNER JOIN (").append(queries.get(i)).append(") t_").append(i) //
                        .append(" ON t_").append(i - 1).append(".id = t_").append(i).append(".id");
            }

            sb.append(")");
            query = sb.toString();
        }

        return query;
    }

}
