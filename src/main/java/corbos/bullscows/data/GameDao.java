package corbos.bullscows.data;

import corbos.bullscows.models.Game;
import corbos.bullscows.models.Round;
import java.sql.SQLException;
import java.util.List;


public interface GameDao {
    
    List<Game> getAllGames();
    
    Game getGameById(int id);
    
    Game addGame();
    
    void updateGame(Game game);
    
    void deleteGameById(int id);
    
    List<Round> getAssociatedRounds(int gameId) throws SQLException;
}



