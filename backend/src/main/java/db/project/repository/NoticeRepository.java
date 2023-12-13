package db.project.repository;

import db.project.dto.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NoticeRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public NoticeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getNoticeCount() {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String sql = "SELECT count(notice_id) noticeCount FROM Notice";

        return jdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
    }

    public Optional<List<NoticeDto.NoticeList>> noticeList(int page) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("page", page);
        String sql = "SELECT notice_id, title, created_at as date, views FROM notice ORDER BY notice_id LIMIT :page, 10";

        try {
            List<NoticeDto.NoticeList> noticeListDto = jdbcTemplate.query(sql, namedParameters, new BeanPropertyRowMapper<>(NoticeDto.NoticeList.class));
            return Optional.of(noticeListDto);
        } catch (BadSqlGrammarException e) {
            return Optional.empty();
        }
    }

    public Optional<NoticeDto.NoticeInfo> noticeInfo(int noticeId) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("noticeId", noticeId);
        String sql = "SELECT notice_id, admin_id AS user_id, title, content, created_at as date, views FROM notice WHERE notice_id =:noticeId";
        try{
            NoticeDto.NoticeInfo noticeInfoDto = jdbcTemplate.queryForObject(sql, namedParameters, new BeanPropertyRowMapper<>(NoticeDto.NoticeInfo.class));

            return Optional.of(noticeInfoDto);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void noticeCreate(NoticeDto.NoticeCreateAndUpdate noticeCreateDto, String user_id) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("title", noticeCreateDto.getTitle())
                .addValue("content", noticeCreateDto.getContent())
                .addValue("user_id", user_id);
        String sql = "INSERT INTO notice(admin_id, title, content) values(:user_id, :title, :content)";
        jdbcTemplate.update(sql, namedParameters);
    }

    public Optional<String> isAuthor(int noticeId) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("noticeId", noticeId);
        String sql = "SELECT admin_id AS user_id FROM notice WHERE notice_id =:noticeId";

        try {
            String user_id = jdbcTemplate.queryForObject(sql, namedParameters, String.class);
            return Optional.of(user_id);
        } catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void noticeUpdate(NoticeDto.NoticeCreateAndUpdate noticeUpdateDto, int noticeId) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("title", noticeUpdateDto.getTitle())
                .addValue("content", noticeUpdateDto.getContent())
                .addValue("noticeId", noticeId);
        String sql = "UPDATE notice SET title =:title, content =:content WHERE notice_id =:noticeId";

        int rowsUpdated = jdbcTemplate.update(sql, namedParameters);
    }

    public void noticeDelete(int noticeId) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("noticeId", noticeId);
        String sql = "DELETE FROM notice WHERE notice_id =:noticeId";

        int rowsUpdated = jdbcTemplate.update(sql, namedParameters);
    }

    public Optional<Integer> getAdminIdAndNoticeId(int noticeId, String user_id) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("noticeId", noticeId)
                .addValue("user_id", user_id);

        String sql = "SELECT view_id FROM notice_views WHERE notice_id =:noticeId AND admin_id =:user_id";

        try {
            int view_id = jdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
            return Optional.of(view_id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Integer> insertNoticeViews(int noticeId, String user_id) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("noticeId", noticeId)
                .addValue("user_id", user_id);

        String sql = "INSERT INTO notice_views(notice_id, admin_id) VALUES(:noticeId, :user_id)";

        try{
            int check = jdbcTemplate.update(sql, namedParameters);
            return Optional.of(check);
        } catch (DataIntegrityViolationException e) {
            return Optional.empty();
        }

    }

    public void updateNoticeView(int noticeId) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("noticeId", noticeId);

        String sql = "UPDATE notice SET views = views + 1 WHERE notice_id =:noticeId";

        jdbcTemplate.update(sql, namedParameters);
    }
}
