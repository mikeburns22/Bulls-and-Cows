package corbos.bullscows.data;


import corbos.bullscows.data.RoundDaoDB.RoundMapper;
import corbos.bullscows.models.Game;
import corbos.bullscows.models.Round;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public abstract class GameDaoDB implements GameDao {
    
    private final JdbcTemplate jdbc;
    
    @Autowired
    public GameDaoDB(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }
    
    @Override
    @Transactional
    public Game addGame() {
        
        Game game = new Game();
        game.setAnswer(generateAnswer());
        game.setFinished(false);
        
        String INSERT_NEW_GAME  = "INSERT INTO Game (GameDone, FinalAnswer VALUES(? , ?);";
        
        jdbc.update(INSERT_NEW_GAME, game.isFinished(), game.getAnswer());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        game.setId(newId);
        return hideAnswerIfUnfinished(game);
    }
    
    private String generateAnswer() {
        
        List<Integer> num = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            num.add(i);
        }
        Collections.shuffle(num);
        
        String ansString = "";
        for(int i = 0; i < 4; i++) {
            ansString += num.get(i).toString();
        }
        return ansString;
    }
    
    @Override
    public List<Game> getAllGames() {
        
        String GET_ALL_GAMES = "SELECT * FROM  Game";
        
        return hideAnswerIfUnfinished(jdbc.query(GET_ALL_GAMES, new GameMapper()));
    }
    
    @Override
    public Game getGameById(int id) {
        
        try {
            
            String GET_GAME_BY_ID = "SELECT * FROM Game WHERE ID = ?";
            
            Game game = jdbc.queryForObject(GET_GAME_BY_ID, new GameMapper(), id);
            
            return hideAnswerIfUnfinished(game);
        } catch (DataAccessException e) {
            return null;
        }
    }
    
    private Game hideAnswerIfUnfinished(Game game) {
        
        if (game == null) {
            return null;
        } else if (!game.isFinished()) {
            game.setAnswer("----");
        }
        
        return game;
    }
    
    private List<Game> hideAnswerIfUnfinished(List<Game> games) {
        
        List<Game> update = new ArrayList<>();
        
        for(Game game : games) {
            update.add(hideAnswerIfUnfinished(game));
        }
        
        return update;
    }
    
    @Override
    public List<Round> getAssociatedRounds(int gameId) throws SQLException {
        
        String GET_ALL_ROUNDS_FOR_GAME = "SELECT r.ID, r.GameID, r.Guess, r.result, r.DateTime" +
                "FROM Round AS r INNER JOIN Game AS g ON g.ID = r.GameID WHERE g.ID = ?";
        
        return jdbc.query(GET_ALL_ROUNDS_FOR_GAME, new RoundMapper(), gameId);
    }
    
    @Override
    public void updateGame(Game game) {
        String UPDATE_GAME = "UPDATE Game SET GameDone = ?, FinalAnswer = ? WHERE ID = ?; ";
        
        jdbc.update(UPDATE_GAME,
                game.isFinished(),
                game.getAnswer(),
                game.getId());
    }
    
    @Override
    @Transactional
    public void deleteGameById(int id) {
        
        String DELETE_ROUND = "DELETE FROM Round WHERE GameID = ?";
        jdbc.update(DELETE_ROUND, id);
        
        String DELETE_GAME = "DELETE FROM Game WHERE  ID = ?";
        jdbc.update(DELETE_GAME, id);
    }
    
    public static final class GameMapper implements RowMapper<Game> {
        
        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            
            Game game = new Game();
            game.setId(rs.getInt("id"));
            game.setFinished(rs.getBoolean("finished"));
            game.setAnswer(rs.getString("finalAnswer"));
            return game;
        }
    }
    
}
