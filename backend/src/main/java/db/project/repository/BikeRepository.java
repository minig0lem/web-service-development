package db.project.repository;

import db.project.dto.PostBikeCreateDto;
import db.project.dto.PostBikeDeleteDto;
import db.project.dto.ReturnGetBikeListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BikeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public int findBikeCountByStatus() {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String sql = "SELECT COUNT(bike_id) bikeCount FROM bike WHERE status IN ('available', 'closed')";

        return jdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
    }

    public Optional<List<ReturnGetBikeListDto>> findBikeByStatus(int page) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("page", page);
        String sql = "SELECT bike_id, address, b.status FROM bike b JOIN location l ON b.location_id = l.location_id " +
                "WHERE b.status IN ('available', 'closed') ORDER BY bike_id LIMIT :page, 10";

        try {
            List<ReturnGetBikeListDto> returnGetBikeListDto = jdbcTemplate.query(sql, namedParameters, new BeanPropertyRowMapper<>(ReturnGetBikeListDto.class));
            return Optional.of(returnGetBikeListDto);
        } catch (BadSqlGrammarException e) {
            return Optional.empty();
        }

    }

    public Optional<String> createBike(PostBikeCreateDto postBikeCreateDto) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("bike_id", postBikeCreateDto.getBike_id())
                .addValue("location_id", postBikeCreateDto.getLocation_id());
        String sql = "INSERT INTO bike(bike_id, location_id) values(:bike_id, :location_id)";

        try{
            jdbcTemplate.update(sql, namedParameters);
            return Optional.of("자전거 생성 성공");
        } catch (DuplicateKeyException e) {
            return Optional.of("bike_id 중복");
        } catch (DataIntegrityViolationException e) {
            return Optional.of("존재하지 않는 location_id");
        }

    }

    public int updateStatusDeletedByIdAndStatus(PostBikeDeleteDto postBikeDeleteDto) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("bike_id", postBikeDeleteDto.getBike_id());

        String sql = "UPDATE bike SET status = 'deleted' WHERE bike_id =:bike_id AND status IN ('available', 'closed')";

        return jdbcTemplate.update(sql, namedParameters);
    }

    public void updateStatusRentedById(String bikeId) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("bike_id", bikeId);

        String updateBikeSql = "UPDATE bike SET status = 'rented' WHERE  bike_id = :bike_id";

        int rowsUpdated = jdbcTemplate.update(updateBikeSql, namedParameters);
    }

    public Optional<String> updateStatusClosedById(String bikeId) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("bike_id", bikeId);

        String updateBikeSql = "UPDATE bike SET status = 'closed' WHERE bike_id = :bike_id";
        int rowsUpdated = jdbcTemplate.update(updateBikeSql, namedParameters);

        return (rowsUpdated > 0) ? Optional.of("자전거 상태가 업데이트되었습니다.") : Optional.empty();
    }

    public void updateLocationAndStatusById(String bikeId, String locationId) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("bike_id", bikeId)
                .addValue("location_id", locationId);

        String updateBikesql = "UPDATE bike SET location_id = :location_id, status='available' WHERE bike_id = :bike_id";
        jdbcTemplate.update(updateBikesql, namedParameters);
    }
}
