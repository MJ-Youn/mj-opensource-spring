package io.github.mjyoun.spring.repository;

import java.util.List;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Oracle 사용을 위한 Repository
 * 
 * @author MJ Youn
 * @since 2022. 05. 25.
 */
public class OracleRepository extends GenericRepository {

    /** pagination 설정을 위한 테이블 이름 */
    private final String paginationTableName = "pagination_table";

    /**
     * (non-javadoc)
     * 
     * @param jdbcTemplate
     *            {@link JdbcTemplate}
     * @param messageSource
     *            Query를 properties로 부터 가져오기 위한 Message Source
     * 
     * @author MJ Youn
     * @since 2022. 05. 25.
     */
    protected OracleRepository(JdbcTemplate jdbcTemplate, //
            ReloadableResourceBundleMessageSource messageSource) {
        super(jdbcTemplate, messageSource);
    }

    /**
     * @see GenericRepository#createPaginationPreFixQuery()
     * 
     * @author MJ Youn
     * @since 2022. 05. 25.
     */
    @Override
    protected String createPaginationPreFixQuery() {
        return new StringBuffer() //
                .append("SELECT ") //
                .append(paginationTableName) //
                .append(".*, ROWNUM FROM ( ") //
                .toString();
    }

    /**
     * @see GenericRepository#createPagenationPostFixQuery(Pageable)
     * 
     * @author MJ Youn
     * @since 2022. 05. 25.
     */
    @Override
    protected String createPagenationPostFixQuery(Pageable pageable) {
        StringBuffer sb = new StringBuffer();

        // order by 추가
        if (pageable.getSort() != null) {
            List<Order> orders = pageable.getSort().toList();

            if (orders.size() > 0) {
                sb.append(" ORDER BY ");

                for (int i = 0; i < orders.size(); i++) {
                    Order order = orders.get(i);

                    sb.append("\"").append(order.getProperty()).append("\"") //
                            .append(" ") //
                            .append(order.getDirection().name());

                    if (i != orders.size() - 1) {
                        sb.append(", ");
                    }
                }
            }
        }

        // pagination 정보 추가
        sb.append(" ) ") //
                .append(paginationTableName) //
                .append(" WHERE ") //
                .append("ROWNUM >= ") //
                .append(pageable.getPageNumber() * pageable.getPageSize()) //
                .append(" AND ") //
                .append("ROWNUM < ") //
                .append((pageable.getPageNumber() + 1) * pageable.getPageSize());

        return sb.toString();
    }

}
