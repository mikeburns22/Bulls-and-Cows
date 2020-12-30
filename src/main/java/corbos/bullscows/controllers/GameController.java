package corbos.bullscows.controllers;


import corbos.bullscows.data.GameDao;
import corbos.bullscows.data.RoundDao;
import corbos.bullscows.models.Game;
import corbos.bullscows.models.Round;


import java.sql.SQLException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private final GameDao gameDao;

    @Autowired
    private final RoundDao roundDao;

    public GameController(GameDao gameDao, RoundDao roundDao) {
        this.gameDao = gameDao;
        this.roundDao = roundDao;
    }

    @CrossOrigin
    @PostMapping("/start")
    public ResponseEntity newGame() {
        Game game = gameDao.addGame();
        return new ResponseEntity(game.getId(), HttpStatus.CREATED);
    }

    @CrossOrigin
    @PostMapping("/guess")
    public ResponseEntity makeGuess(@RequestBody Round round) {
        Game game = gameDao.getGameById(round.getGameId());
        if (game == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        round = roundDao.addRound(round);
        return ResponseEntity.ok(round);
    }

    @CrossOrigin
    @GetMapping("/game")
    public ResponseEntity getGames() {
        return ResponseEntity.ok(gameDao.getAllGames());
    }

    @CrossOrigin
    @GetMapping("/game/{GameID}")
    public ResponseEntity getGameById(@PathVariable int gameId) {
        Game game = gameDao.getGameById(gameId);
        if (game == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(game);
    }

    @CrossOrigin
    @GetMapping("/rounds/{GameID}")
    public ResponseEntity getRoundsByGameId(@PathVariable int gameId) {
        try {
            return ResponseEntity.ok(gameDao.getAssociatedRounds(gameId));
        } catch (SQLException e) {
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
        }
    }
}


