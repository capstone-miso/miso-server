package capstone.miso.dishcovery.application.files.repository;

import capstone.miso.dishcovery.domain.keyword.dao.KeywordDataDAO;
import capstone.miso.dishcovery.domain.keyword.dao.KeywordManagerDAO;
import capstone.miso.dishcovery.domain.store.dto.StoreTimeTableDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FileDataJdbcRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final static String TABLE = "file_data";
    static final RowMapper<KeywordDataDAO> rowMapperFileDataToKeywordData = (rs, rowNum) -> KeywordDataDAO.builder()
            .storeId(rs.getLong("store_id"))
            .totalVisited(rs.getLong("total_visited"))
            .totalCost(rs.getLong("total_cost"))
            .totalParticipants(rs.getLong("total_participants"))
            .costPerPerson(rs.getLong("cost_per_person"))
            .spring(rs.getLong("spring"))
            .summer(rs.getLong("summer"))
            .fall(rs.getLong("fall"))
            .winter(rs.getLong("winter"))
            .breakfast(rs.getLong("breakfast"))
            .lunch(rs.getLong("lunch"))
            .dinner(rs.getLong("dinner"))
            .smallGroup(rs.getLong("small_group"))
            .mediumGroup(rs.getLong("medium_group"))
            .largeGroup(rs.getLong("large_group"))
            .extraGroup(rs.getLong("extra_group"))
            .costUnder8000(rs.getLong("cost_under_8000"))
            .costUnder15000(rs.getLong("cost_under_15000"))
            .costUnder25000(rs.getLong("cost_under_25000"))
            .costOver25000(rs.getLong("cost_over_25000"))
            .build();

    static final RowMapper<KeywordManagerDAO> rowMapperFileDataToKeywordManager = (rs, rowNum) -> KeywordManagerDAO.builder()
            .totalVisited(rs.getLong("total_visited"))
            .totalCost(rs.getLong("total_cost"))
            .totalParticipants(rs.getLong("total_participants"))
            .costPerPerson(rs.getLong("cost_per_person"))
            .spring(rs.getLong("spring"))
            .summer(rs.getLong("summer"))
            .fall(rs.getLong("fall"))
            .winter(rs.getLong("winter"))
            .breakfast(rs.getLong("breakfast"))
            .lunch(rs.getLong("lunch"))
            .dinner(rs.getLong("dinner"))
            .smallGroup(rs.getLong("small_group"))
            .mediumGroup(rs.getLong("medium_group"))
            .largeGroup(rs.getLong("large_group"))
            .extraGroup(rs.getLong("extra_group"))
            .costUnder8000(rs.getLong("cost_under_8000"))
            .costUnder15000(rs.getLong("cost_under_15000"))
            .costUnder25000(rs.getLong("cost_under_25000"))
            .costOver25000(rs.getLong("cost_over_25000"))
            .build();

    static final RowMapper<StoreTimeTableDTO> rowMapperFileDataToStoreTimeTable = (rs, rowNum) -> StoreTimeTableDTO.builder()
            .under8(rs.getLong("under8"))
            .hour9(rs.getLong("hour9"))
            .hour10(rs.getLong("hour10"))
            .hour11(rs.getLong("hour11"))
            .hour12(rs.getLong("hour12"))
            .hour13(rs.getLong("hour13"))
            .hour14(rs.getLong("hour14"))
            .hour15(rs.getLong("hour15"))
            .hour16(rs.getLong("hour16"))
            .hour17(rs.getLong("hour17"))
            .hour18(rs.getLong("hour18"))
            .hour19(rs.getLong("hour19"))
            .hour20(rs.getLong("hour20"))
            .hour21(rs.getLong("hour21"))
            .after22(rs.getLong("after22"))
            .build();

    public List<KeywordDataDAO> getAllKeywordDataFromFileData() {
        var sql = String.format("""
                SELECT f.store_id                                                                                       AS 'store_id',
                       COUNT(f.fid)                                                                                     AS 'total_visited',
                       SUM(f.cost)                                                                                      AS 'total_cost',
                       SUM(f.participants)                                                                              AS 'total_participants',
                       ROUND(SUM(f.cost) / SUM(f.participants), 0)                                                      as cost_per_person,
                       COUNT(CASE WHEN MONTH(f.date) BETWEEN 3 AND 5 THEN 1 END)                                        AS 'spring',
                       COUNT(CASE WHEN MONTH(f.date) BETWEEN 6 AND 8 THEN 1 END)                                        AS 'summer',
                       COUNT(CASE WHEN MONTH(f.date) BETWEEN 9 AND 11 THEN 1 END)                                       AS 'fall',
                       COUNT(CASE WHEN MONTH(f.date) >= 12 OR MONTH(f.date) <= 2 THEN 1 END)                            AS 'winter',
                       COUNT(CASE WHEN HOUR(f.time) < 11 THEN 1 END)                                                    AS 'breakfast',
                       COUNT(CASE WHEN HOUR(f.time) BETWEEN 11 AND 16 THEN 1 END)                                       AS 'lunch',
                       COUNT(CASE WHEN HOUR(f.time) > 16 THEN 1 END)                                                    AS 'dinner',
                       COUNT(CASE WHEN f.participants < 5 THEN 1 END)                                                   AS 'small_group',
                       COUNT(CASE WHEN f.participants BETWEEN 5 AND 10 THEN 1 END)                                      AS 'medium_group',
                       COUNT(CASE WHEN f.participants BETWEEN 11 AND 20 THEN 1 END)                                     AS 'large_group',
                       COUNT(CASE WHEN f.participants > 20 THEN 1 END)                                                  AS 'extra_group',
                       COUNT(CASE WHEN f.cost / f.participants <= 8000 THEN 1 END)                                      AS 'cost_under_8000',
                       COUNT(CASE
                                 WHEN f.cost / f.participants > 8000 AND f.cost / f.participants <= 15000
                                     THEN 1 END)                                                                        AS 'cost_under_15000',
                       COUNT(CASE
                                 WHEN f.cost / f.participants > 15000 AND f.cost / f.participants <= 25000
                                     THEN 1 END)                                                                        AS 'cost_under_25000',
                       COUNT(CASE WHEN f.cost / f.participants > 25000 THEN 1 END)                                      AS 'cost_over_25000'
                FROM %s f
                WHERE f.store_id IS NOT NULL
                GROUP BY f.store_id
                """, TABLE);
        return namedParameterJdbcTemplate.query(sql, rowMapperFileDataToKeywordData);
    }

    public KeywordManagerDAO getKeywordManagerFromFileData() {
        var sql = String.format("""
                SELECT COUNT(f.fid)                                                                                     AS 'total_visited',
                       SUM(f.cost)                                                                                      AS 'total_cost',
                       SUM(f.participants)                                                                              AS 'total_participants',
                       ROUND(SUM(f.cost) / SUM(f.participants), 0)                                                      as cost_per_person,
                       COUNT(CASE WHEN MONTH(f.date) BETWEEN 3 AND 5 THEN 1 END)                                        AS 'spring',
                       COUNT(CASE WHEN MONTH(f.date) BETWEEN 6 AND 8 THEN 1 END)                                        AS 'summer',
                       COUNT(CASE WHEN MONTH(f.date) BETWEEN 9 AND 11 THEN 1 END)                                       AS 'fall',
                       COUNT(CASE WHEN MONTH(f.date) >= 12 OR MONTH(f.date) <= 2 THEN 1 END)                            AS 'winter',
                       COUNT(CASE WHEN HOUR(f.time) < 11 THEN 1 END)                                                    AS 'breakfast',
                       COUNT(CASE WHEN HOUR(f.time) BETWEEN 11 AND 16 THEN 1 END)                                       AS 'lunch',
                       COUNT(CASE WHEN HOUR(f.time) > 16 THEN 1 END)                                                    AS 'dinner',
                       COUNT(CASE WHEN f.participants < 5 THEN 1 END)                                                   AS 'small_group',
                       COUNT(CASE WHEN f.participants BETWEEN 5 AND 10 THEN 1 END)                                      AS 'medium_group',
                       COUNT(CASE WHEN f.participants BETWEEN 11 AND 20 THEN 1 END)                                     AS 'large_group',
                       COUNT(CASE WHEN f.participants > 20 THEN 1 END)                                                  AS 'extra_group',
                       COUNT(CASE WHEN f.cost / f.participants <= 8000 THEN 1 END)                                      AS 'cost_under_8000',
                       COUNT(CASE
                                 WHEN f.cost / f.participants > 8000 AND f.cost / f.participants <= 15000
                                     THEN 1 END)                                                                        AS 'cost_under_15000',
                       COUNT(CASE
                                 WHEN f.cost / f.participants > 15000 AND f.cost / f.participants <= 25000
                                     THEN 1 END)                                                                        AS 'cost_under_25000',
                       COUNT(CASE WHEN f.cost / f.participants > 25000 THEN 1 END)                                      AS 'cost_over_25000'
                FROM %s f
                WHERE f.store_id IS NOT NULL
                """, TABLE);
        List<KeywordManagerDAO> result = namedParameterJdbcTemplate.query(sql, rowMapperFileDataToKeywordManager);
        if (result.size() == 0)
            return null;
        else return result.get(0);
    }

    public List<KeywordDataDAO> getKeywordDataFromFileData() {
        var sql = String.format("""
                SELECT f.store_id                                                            AS 'store_id',
                       COUNT(f.fid)                                                          AS 'total_visited',
                       SUM(f.cost)                                                           AS 'total_cost',
                       SUM(f.participants)                                                   AS 'total_participants',
                       ROUND(SUM(f.cost) / SUM(f.participants), 0)                           as cost_per_person,
                       COUNT(CASE WHEN MONTH(f.date) BETWEEN 3 AND 5 THEN 1 END)             AS 'spring',
                       COUNT(CASE WHEN MONTH(f.date) BETWEEN 6 AND 8 THEN 1 END)             AS 'summer',
                       COUNT(CASE WHEN MONTH(f.date) BETWEEN 9 AND 11 THEN 1 END)            AS 'fall',
                       COUNT(CASE WHEN MONTH(f.date) >= 12 OR MONTH(f.date) <= 2 THEN 1 END) AS 'winter',
                       COUNT(CASE WHEN HOUR(f.time) < 11 THEN 1 END)                         AS 'breakfast',
                       COUNT(CASE WHEN HOUR(f.time) BETWEEN 11 AND 16 THEN 1 END)            AS 'lunch',
                       COUNT(CASE WHEN HOUR(f.time) > 16 THEN 1 END)                         AS 'dinner',
                       COUNT(CASE WHEN f.participants < 5 THEN 1 END)                        AS 'small_group',
                       COUNT(CASE WHEN f.participants BETWEEN 5 AND 10 THEN 1 END)           AS 'medium_group',
                       COUNT(CASE WHEN f.participants BETWEEN 11 AND 20 THEN 1 END)          AS 'large_group',
                       COUNT(CASE WHEN f.participants > 20 THEN 1 END)                       AS 'extra_group',
                       COUNT(CASE WHEN f.cost / f.participants <= 8000 THEN 1 END)           AS 'cost_under_8000',
                       COUNT(CASE
                                 WHEN f.cost / f.participants > 8000 AND f.cost / f.participants <= 15000
                                     THEN 1 END)                                             AS 'cost_under_15000',
                       COUNT(CASE
                                 WHEN f.cost / f.participants > 15000 AND f.cost / f.participants <= 25000
                                     THEN 1 END)                                             AS 'cost_under_25000',
                       COUNT(CASE WHEN f.cost / f.participants > 25000 THEN 1 END)           AS 'cost_over_25000'
                FROM %s f
                WHERE f.store_id IS NOT NULL
                  AND f.store_id != -1
                  AND f.store_id IN (SELECT f2.store_id
                                     FROM %s f2
                                     WHERE DATE(f2.created_at) = :date
                                     GROUP BY f2.store_id)
                GROUP BY f.store_id
                """, TABLE, TABLE);

        MapSqlParameterSource param = new MapSqlParameterSource().addValue("date", LocalDate.now());
        return namedParameterJdbcTemplate.query(sql, param, rowMapperFileDataToKeywordData);
    }

    public StoreTimeTableDTO getStoreTimeTableDTO(Long storeId) {
        var sql = String.format("""
                SELECT COUNT(CASE WHEN HOUR(f.time) < 8 THEN 1 END)   AS 'under8',
                       COUNT(CASE WHEN HOUR(f.time) = 9 THEN 1 END)   AS 'hour9',
                       COUNT(CASE WHEN HOUR(f.time) = 10 THEN 1 END)  AS 'hour10',
                       COUNT(CASE WHEN HOUR(f.time) = 11 THEN 1 END)  AS 'hour11',
                       COUNT(CASE WHEN HOUR(f.time) = 12 THEN 1 END)  AS 'hour12',
                       COUNT(CASE WHEN HOUR(f.time) = 13 THEN 1 END)  AS 'hour13',
                       COUNT(CASE WHEN HOUR(f.time) = 14 THEN 1 END)  AS 'hour14',
                       COUNT(CASE WHEN HOUR(f.time) = 15 THEN 1 END)  AS 'hour15',
                       COUNT(CASE WHEN HOUR(f.time) = 16 THEN 1 END)  AS 'hour16',
                       COUNT(CASE WHEN HOUR(f.time) = 17 THEN 1 END)  AS 'hour17',
                       COUNT(CASE WHEN HOUR(f.time) = 18 THEN 1 END)  AS 'hour18',
                       COUNT(CASE WHEN HOUR(f.time) = 19 THEN 1 END)  AS 'hour19',
                       COUNT(CASE WHEN HOUR(f.time) = 20 THEN 1 END)  AS 'hour20',
                       COUNT(CASE WHEN HOUR(f.time) = 21 THEN 1 END)  AS 'hour21',
                       COUNT(CASE WHEN HOUR(f.time) >= 22 THEN 1 END) AS 'after22'
                FROM %s f
                WHERE store_id = :storeId
                """, TABLE);
        MapSqlParameterSource param = new MapSqlParameterSource().addValue("storeId", storeId);
        return namedParameterJdbcTemplate.queryForObject(sql, param, rowMapperFileDataToStoreTimeTable);
    }
}
