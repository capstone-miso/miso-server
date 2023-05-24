package capstone.miso.dishcovery.domain.store.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreJDBCRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Long> findSimilarWithNowStore(Long storeId, String category) {
        var sql = """
                select p.store_id
                from preference p
                where p.store_id in (select s.sid
                                     from store s
                                              inner join store_keyword sk on s.sid = sk.store_id
                                     where sk.keyword in ( select * from (select ss.keyword
                                                          from store_keyword ss
                                                          where ss.store_id = :storeId
                                                          limit 2) as tmp)
                                       AND s.category like :category)
                group by p.store_id
                order by count(p.store_id) desc
                limit 5
                """;
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("storeId", storeId)
                .addValue("category", category);
        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> rs.getLong("store_id"));
    }
}
