package io.github.mjyoun.spring.repository;

import java.util.List;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * PostgreSQL 사용을 위한 Repository
 * 
 * @author MJ Youn
 * @since 2021. 12. 27.
 */
public class PostgreSQLRepository extends GenericRepository {

    /**
     * @param jdbcTemplate JdbcTemplate
     * @param messageSource Query를 properties로 부터 가져오기 위한 Message Source
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    protected PostgreSQLRepository(JdbcTemplate jdbcTemplate, //
            ReloadableResourceBundleMessageSource messageSource) {
        super(jdbcTemplate, messageSource);
    }

    /**
     * @see GenericRepository#createPagenationQuery(Pageable)
     * 
     * @author MJ Youn
     * @since 2021. 12. 27.
     */
    @Override
    protected String createPagenationQuery(Pageable pageable) {
        StringBuffer sb = new StringBuffer();

        // order by 추가
        if (pageable.getSort() != null) {
            List<Order> orders = pageable.getSort().toList();

            if (orders.size() > 0) {
                sb.append(" ORDER BY ");

                for (int i = 0; i < orders.size(); i++) {
                    Order order = orders.get(i);
                    sb //
                            .append(order.getProperty()) //
                            .append(" ") //
                            .append(order.getDirection().name());

                    if (i != orders.size() - 1) {
                        sb.append(", ");
                    }
                }
            }
        }

        // pagination 정보 추가
        sb //
                .append(" LIMIT ") //
                .append(pageable.getPageSize()) //
                .append(" OFFSET ") //
                .append(pageable.getPageNumber() * pageable.getPageSize());

        return sb.toString();
    }

}
