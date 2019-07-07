package yashbhandari.mafioso.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yashbhandari.mafioso.Game.Role;
import yashbhandari.mafioso.Requests.CreateRequest;
import yashbhandari.mafioso.Service.GameService;
import yashbhandari.mafioso.TransferObject.PlayerAndRoleList;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(exposedHeaders = {"Location", "gameCode", "role"})
@RequestMapping("/mafioso")
public class GameController {

    public static final String baseURL = "https://mafioso-app.herokuapp.com/mafioso/";

    private Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private GameService gameService;

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createGame(@RequestBody CreateRequest req) throws Exception{
        String gameCode = gameService.createGame(req.getRoles());
        return ResponseEntity.created(new URI(baseURL + gameCode))
                .header("gameCode", gameCode)
                .build();
    }

    @RequestMapping(path = "/join/{gameCode}", method = RequestMethod.POST)
    public ResponseEntity<Void> joinGame(@PathVariable(name="gameCode") String gameCode,
                                         @RequestParam(name="playerName") String playerName) throws Exception{
        int playerId = gameService.addPlayer(gameCode, playerName);

        if(playerId == -1)
            return ResponseEntity.status(HttpStatus.GONE).build();

        String role = gameService.getRoleList(gameCode, playerId).get(0).getRoleName();
        return ResponseEntity.created(new URI(baseURL+gameCode+"/"+playerId))
                .header("role", role)
                .header("playerId", String.valueOf(playerId))
                .build();
    }

    @RequestMapping(path = "/{gameCode}/{playerId}", method = RequestMethod.GET)
    public List<Role> getRole(@PathVariable(name = "gameCode") String gameCode,
                        @PathVariable(name="playerId") int playerId) {
        return gameService.getRoleList(gameCode, playerId);
    }

    @RequestMapping(path = "/{gameCode}", method = RequestMethod.GET)
    public ResponseEntity<PlayerAndRoleList> getPlayersAndRoles(@PathVariable(name = "gameCode") String gameCode){
        logger.info("tried to get game");

        if (!gameService.validCode(gameCode))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(gameService.getPlayerAndRoleList(gameCode));
    }

}
