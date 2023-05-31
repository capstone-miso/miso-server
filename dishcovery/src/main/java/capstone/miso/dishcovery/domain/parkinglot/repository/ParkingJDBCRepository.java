package capstone.miso.dishcovery.domain.parkinglot.repository;

import capstone.miso.dishcovery.domain.parkinglot.dto.ParkingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ParkingJDBCRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    static final RowMapper<ParkingDTO> ROW_MAPPER_PARKING_LOT = (rs, rowNum) -> ParkingDTO.builder()
            .parkingName(rs.getString("parking_name"))
            .address(rs.getString("address"))
            .lon(rs.getDouble("lon"))
            .lat(rs.getDouble("lat"))
            .build();

    public ParkingDTO findCloseParkinglot(Double lon, Double lat) {
        var sql = """
                SELECT *
                FROM parkinglot p
                WHERE (
                              6371 * ACOS(
                                          COS(RADIANS(:latitude)) * COS(RADIANS(p.lat)) * COS(RADIANS(p.lon) - RADIANS(:longitude)) +
                                          SIN(RADIANS(:latitude)) * SIN(RADIANS(p.lat))
                              )
                          ) <= 1
                order by (
                                 6371 * ACOS(
                                             COS(RADIANS(:latitude)) * COS(RADIANS(p.lat)) * COS(RADIANS(p.lon) - RADIANS(:longitude)) +
                                             SIN(RADIANS(:latitude)) * SIN(RADIANS(p.lat))
                                 )
                             ) asc
                limit 1;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("latitude", lat)
                .addValue("longitude", lon);
        return namedParameterJdbcTemplate.queryForObject(sql, params, ROW_MAPPER_PARKING_LOT);
    }

    public List<ParkingDTO> findAreaParkinglot(Double lon, Double lat) {
        var sql = """
                SELECT *
                FROM parkinglot p
                WHERE (
                              6371 * ACOS(
                                          COS(RADIANS(:latitude)) * COS(RADIANS(p.lat)) * COS(RADIANS(p.lon) - RADIANS(:longitude)) +
                                          SIN(RADIANS(:latitude)) * SIN(RADIANS(p.lat))
                              )
                          ) <= 2
                order by (
                                 6371 * ACOS(
                                             COS(RADIANS(:latitude)) * COS(RADIANS(p.lat)) * COS(RADIANS(p.lon) - RADIANS(:longitude)) +
                                             SIN(RADIANS(:latitude)) * SIN(RADIANS(p.lat))
                                 )
                             ) asc;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("latitude", lat)
                .addValue("longitude", lon);
        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER_PARKING_LOT);
    }
}
