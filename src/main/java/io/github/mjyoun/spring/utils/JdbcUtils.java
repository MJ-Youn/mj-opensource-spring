package io.github.mjyoun.spring.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;

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
    public static <T> void setPreparedStatement(PreparedStatement ps, int index, T data, Class<T> type) throws SQLException {
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
    public static String getString(ResultSet rs, String columnName) {
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
    public static Long getLong(ResultSet rs, String columnName) {
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
    public static Integer getInteger(ResultSet rs, String columnName) {
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
    public static Boolean getBoolean(ResultSet rs, String columnName) {
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
    public static LocalDateTime getLocalDateTime(ResultSet rs, String columnName) {
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
    public static Float getFloat(ResultSet rs, String columnName) {
        try {
            float data = rs.getFloat(columnName);
            return data;
        } catch (SQLException ignore) {
            return null;
        }
    }

}
