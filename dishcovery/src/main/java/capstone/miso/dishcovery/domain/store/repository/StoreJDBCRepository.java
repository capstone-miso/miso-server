package capstone.miso.dishcovery.domain.store.repository;

import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreJDBCRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Long> findSimilarWithNowStore(Long storeId, String category, Double lat, Double lon) {
        var sql = """
                select sss.storeId from (
                       select p.store_id as 'storeId', s2.lat, s2.lon
                       from preference p
                                left join store s2 on p.store_id = s2.sid
                       where p.store_id in (select s.sid
                                            from store s
                                                     inner join store_keyword sk on s.sid = sk.store_id
                                            where sk.keyword in (select *
                                                                 from (select ss.keyword
                                                                       from store_keyword ss
                                                                       where ss.store_id = :storeId
                                                                       limit 2) as tmp)
                                              AND s.category like :category
                                              AND s.sid != :storeId)
                       group by p.store_id) as sss
                       group by sss.storeId, sss.lat, sss.lon
                       order by (abs(sss.lat - :lat) + abs(sss.lon - :lon)) asc
                       limit 5
                  """;
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("storeId", storeId)
                .addValue("category", "%" + category + "%")
                .addValue("lat", lat)
                .addValue("lon", lon);
        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> rs.getLong("storeId"));
    }

    public List<Long> findSimilarWithNowStoreOnlyCategory(String category, Double lat, Double lon) {
        var sql = """
                select ss.sid from
                (select s.sid, s.lat, s.lon
                from store s
                         left join preference p on s.sid = p.store_id
                where s.category like :category
                and s.sid != :sid
                group by s.sid) as ss
                group by ss.sid, ss.lat, ss.lon
                order by (abs(ss.lat - :lat) + abs(ss.lon - :lon)) asc,
                         count(ss.sid) desc
                limit 5
                """;
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("category", "%" + category + "%")
                .addValue("lat", lat)
                .addValue("lon", lon);
        return namedParameterJdbcTemplate.query(sql, param, (rs, rowNum) -> rs.getLong("storeId"));
    }
}
