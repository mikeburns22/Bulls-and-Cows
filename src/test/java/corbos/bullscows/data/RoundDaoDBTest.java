package corbos.bullscows.data;


import corbos.bullscows.TestAppConfig;
import corbos.bullscows.models.Game;
import corbos.bullscows.models.Round;


import java.util.List;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestAppConfig.class)
public class RoundDaoDBTest {
    
    @Autowired
    GameDao gameDao;
    
    @Autowired
    RoundDao roundDao;
    
    public RoundDaoDBTest() {
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
    public void testAddGetRound() {
        Game game = gameDao.addGame();
        Round round = new Round();
        round.setGuess("1234");
        round.setGameId(game.getId());
        round = roundDao.addRound(round);
        
        Round fromDao = roundDao.getRoundById(round.getId());
        
        assertEquals(round, fromDao);
    }
    
    @Test
    public void testGetAllRounds() {
        Game game = gameDao.addGame();
        Round r1 = new Round();
        r1.setGuess("1234");
        r1.setGameId(game.getId());
        r1 = roundDao.addRound(r1);
        Round r2 = new Round();
        r2.setGuess("4321");
        r2.setGameId(game.getId());
        r2 = roundDao.addRound(r2);
        
        List<Round> rounds = roundDao.getAllRounds();
        
        assertEquals(2, rounds.size());
        assertTrue(rounds.contains(r1));
        assertTrue(rounds.contains(r2));
    }
    
    @Test
    public void testUpdateRound() {
        Game game = gameDao.addGame();
        Round round = new Round();
        round.setGuess("1234");
        round.setGameId(game.getId());
        round = roundDao.addRound(round);
        Round fromDao = roundDao.getRoundById(round.getId());
        assertEquals(round, fromDao);
        
        round.setGuess("4321");
        roundDao.updateRound(round);
        assertNotEquals(round, fromDao);
        
        fromDao = roundDao.getRoundById(round.getId());
        assertEquals(round, fromDao);
    }
    
    @Test
    public void testRoundResults() {
        Game game = gameDao.addGame();
        game.setAnswer("1234");
        gameDao.updateGame(game);
        
        Round oneExact = new Round();
        oneExact.setGuess("1567");
        oneExact.setGameId(game.getId());
        oneExact = roundDao.addRound(oneExact);
        game = gameDao.getGameById(game.getId());
        
        assertEquals(oneExact.getResult(), "e:1:p:0");
        assertFalse(game.isFinished());
        
        Round onePartial = new Round();
        onePartial.setGuess("5671");
        onePartial.setGameId(game.getId());
        onePartial = roundDao.addRound(onePartial);
        game = gameDao.getGameById(game.getId());
        
        assertEquals(onePartial.getResult(), "e:0:p:1");
        assertFalse(game.isFinished());
        
        Round oneExactOnePartial = new Round();
        oneExactOnePartial.setGuess("1456");
        oneExactOnePartial.setGameId(game.getId());
        oneExactOnePartial = roundDao.addRound(oneExactOnePartial);        
        game = gameDao.getGameById(game.getId());
        
        assertEquals(oneExactOnePartial.getResult(), "e:1:p:1");
        assertFalse(game.isFinished());
        
        Round allPartial = new Round();
        allPartial.setGuess("4321");
        allPartial.setGameId(game.getId());
        allPartial = roundDao.addRound(allPartial);
        game = gameDao.getGameById(game.getId());
        
        assertEquals(allPartial.getResult(), "e:0:p:4");
        assertFalse(game.isFinished());
        
        Round allExact = new Round();
        allExact.setGuess("1234");
        allExact.setGameId(game.getId());
        allExact = roundDao.addRound(allExact);
        game = gameDao.getGameById(game.getId());
        
        assertEquals(allExact.getResult(), "e:4:p:0");
        assertTrue(game.isFinished());
    }
    
    @Test
    public void testDeleteRound() {
        Game game = gameDao.addGame();
        Round round = new Round();
        round.setGuess("1234");
        round.setGameId(game.getId());
        round = roundDao.addRound(round);
        
        roundDao.deleteRoundById(round.getId());
        assertNull(roundDao.getRoundById(round.getId()));
    }
    
}