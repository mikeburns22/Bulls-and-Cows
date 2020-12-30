package corbos.bullscows.data;


import corbos.bullscows.TestAppConfig;
import corbos.bullscows.models.Game;
import corbos.bullscows.models.Round;


import java.sql.SQLException;
import java.util.List;


import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestAppConfig.class)
public class GameDaoDBTest {
    
    @Autowired
    GameDao gameDao;
    
    @Autowired
    RoundDao roundDao;
    
    public GameDaoDBTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        List<Round> rounds = roundDao.getAllRounds();
        for (Round round : rounds) {
            roundDao.deleteRoundById(round.getId());
        }
        
        List<Game> games = gameDao.getAllGames();
        for (Game game : games) {
            gameDao.deleteGameById(game.getId());
        }
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testAddGetGame() {
        Game game = gameDao.addGame();
        Game fromDao = gameDao.getGameById(game.getId());
        
        assertEquals(game, fromDao);
    }
    
    @Test
    public void testGetAllGames() {
        Game game1 = gameDao.addGame();
        Game game2 = gameDao.addGame();
        
        List<Game> games = gameDao.getAllGames();
        
        assertEquals(2, games.size());        
        assertTrue(games.contains(game1));
        assertTrue(games.contains(game2));
    }
    
    @Test
    public void testGetAssociatedRounds() {
        Game game = gameDao.addGame();
        int id = game.getId();
        
        Round r1 = new Round();
        r1.setGuess("1234");
        r1.setGameId(id);
        r1 = roundDao.addRound(r1);
        
        Round r2 = new Round();
        r2.setGuess("2345");
        r2.setGameId(id);
        r2 = roundDao.addRound(r2);
        
        try {
            List<Round> rounds = gameDao.getAssociatedRounds(id);
            assertEquals(2, rounds.size());
            assertTrue(rounds.contains(r1));
            assertTrue(rounds.contains(r2));
        } catch (SQLException e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testUpdateGame() {
        Game game = gameDao.addGame();
        game.setFinished(true);
        game.setAnswer("4321");
        gameDao.updateGame(game);
        
        Game fromDao = gameDao.getGameById(game.getId());
        assertEquals(game, fromDao);
        
        game.setAnswer("1234");
        gameDao.updateGame(game);
        assertNotEquals(game, fromDao);
        
        fromDao = gameDao.getGameById(game.getId());
        assertEquals(game, fromDao);
    }
    
    @Test
    public void testAnswerHiding() {
        Game game = gameDao.addGame();
        assertEquals(game.getAnswer(), "----");
        
        game.setFinished(true);
        gameDao.updateGame(game);
        game = gameDao.getGameById(game.getId());
        assertNotEquals(game.getId(), "----");
    }
    
    @Test
    public void testDeleteGame() {
        Game game = gameDao.addGame();
        
        Round round = new Round();
        round.setGameId(game.getId());
        round.setGuess("1234");
        round = roundDao.addRound(round);
        
        gameDao.deleteGameById(game.getId());
        
        assertNull(gameDao.getGameById(game.getId()));
        assertNull(roundDao.getRoundById(round.getId()));        
    }
    
}