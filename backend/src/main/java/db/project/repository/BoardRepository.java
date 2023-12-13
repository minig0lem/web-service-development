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
public class BoardRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BoardRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getBoardCount() {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String sql = "SELECT count(board_id) boardCount FROM board";

        return jdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
    }

    public Optional<List<BoardDto.BoardList>> boardList(int page) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("page", page);
        String sql = "SELECT board_id, title, created_at as date, views FROM board ORDER BY board_id LIMIT :page, 10";

        try {
            List<BoardDto.BoardList> boardListDto = jdbcTemplate.query(sql, namedParameters, new BeanPropertyRowMapper<>(BoardDto.BoardList.class));
            return Optional.of(boardListDto);
        } catch (BadSqlGrammarException e) {
            return Optional.empty();
        }
    }

    public Optional<BoardDto.BoardInfo> boardInfo(int board_id) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("board_id", board_id);
        String sql = "SELECT board_id, user_id, title, content, created_at as date, views FROM board WHERE board_id =:board_id";
        try{
            BoardDto.BoardInfo boardInfoDto = jdbcTemplate.queryForObject(sql, namedParameters, new BeanPropertyRowMapper<>(BoardDto.BoardInfo.class));

            return Optional.of(boardInfoDto);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void boardCreate(BoardDto.BoardCreateAndUpdate boardCreateDto, String user_id) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("title", boardCreateDto.getTitle())
                .addValue("content", boardCreateDto.getContent())
                .addValue("user_id", user_id);
        String sql = "INSERT INTO board(user_id, title, content) values(:user_id, :title, :content)";
        jdbcTemplate.update(sql, namedParameters);
    }

    public void boardUpdate(BoardDto.BoardCreateAndUpdate boardUpdateDto, int boardId) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("title", boardUpdateDto.getTitle())
                .addValue("content", boardUpdateDto.getContent())
                .addValue("boardId", boardId);
        String sql = "UPDATE board SET title =:title, content =:content WHERE board_id =:boardId";

        int rowsUpdated = jdbcTemplate.update(sql, namedParameters);
    }

    public void boardDelete(BoardDto.BoardDelete boardDeleteDto) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("boardId", boardDeleteDto.getBoard_id());
        String sql = "DELETE FROM board WHERE board_id =:boardId";

        int rowsUpdated = jdbcTemplate.update(sql, namedParameters);
    }

    public Optional<String> isAuthor(int boardId) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("boardId", boardId);
        String sql = "SELECT user_id FROM board WHERE board_id =:boardId";

        try {
            String user_id = jdbcTemplate.queryForObject(sql, namedParameters, String.class);
            return Optional.of(user_id);
        } catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Integer> getUserIdAndBoardId(int boardId, String user_id) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("boardId", boardId)
                .addValue("user_id", user_id);

        String sql = "SELECT view_id FROM board_views WHERE board_id =:boardId AND user_id = user_id";

        try {
            int view_id = jdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
            return Optional.of(view_id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Integer> insertBoardViews(int boardId, String user_id) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("boardId", boardId)
                .addValue("user_id", user_id);

        String sql = "INSERT INTO board_views(board_id, user_id) VALUES(:boardId, :user_id)";

        try{
            int check = jdbcTemplate.update(sql, namedParameters);
            return Optional.of(check);
        } catch (DataIntegrityViolationException e) {
            return Optional.empty();
        }
    }

    public void updateBoardView(int boardId) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("boardId", boardId);

        String sql = "UPDATE board SET views = views + 1 WHERE board_id =:boardId";

        jdbcTemplate.update(sql, namedParameters);
    }
}
