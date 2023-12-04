package db.project.repository;

import db.project.dto.PostRentalHistoryDto;
import db.project.dto.ReturnPostRentalHistoryDto;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class RentalHistoryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RentalHistoryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ReturnPostRentalHistoryDto> rentalHistory(PostRentalHistoryDto postRentalHistoryDto, String user_id) {

        String endDate = LocalDate.parse(postRentalHistoryDto.getEnd_date()).plusDays(1).toString();

        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("start_date", postRentalHistoryDto.getStart_date())
                .addValue("end_date", endDate)
                .addValue("user_id", user_id);

        String sql = "SELECT bike_id, start_time, start_location, end_time, end_location FROM rental " +
                "WHERE user_id =:user_id AND start_time BETWEEN DATE(:start_date) AND DATE(:end_date) " +
                "AND end_location IS NOT NULL ORDER BY start_time";

        return jdbcTemplate.query(sql, namedParameters, new BeanPropertyRowMapper<>(ReturnPostRentalHistoryDto.class));
    }
}