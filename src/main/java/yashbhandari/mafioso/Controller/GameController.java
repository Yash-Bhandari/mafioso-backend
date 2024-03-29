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
@CrossOrigin(exposedHeaders = {"Location", "gameCode", "role", "id"})
@RequestMapping("/mafioso")
public class GameController {

    public static final String baseURL = "https://mafioso-app.herokuapp.com/mafioso/";

    private Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private GameService gameService;

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createGame(@RequestBody CreateRequest req) throws Exception{
        String[] gameCodeAndId = gameService.createGame(req.getRoles());
        return ResponseEntity.created(new URI(baseURL + gameCodeAndId[0]))
                .header("gameCode", gameCodeAndId[0])
                .header("id", gameCodeAndId[1])
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
                .header("id", String.valueOf(playerId))
                .build();
    }

    @RequestMapping(path = "/{gameCode}/{id}", method = RequestMethod.GET)
    public List<Role> getRole(@PathVariable(name = "gameCode") String gameCode,
                        @PathVariable(name="id") int id) {
        return gameService.getRoleList(gameCode, id);
    }

    @RequestMapping(path = "/{gameCode}/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> killPlayer(@PathVariable(name = "gameCode") String gameCode,
                                           @PathVariable(name="id") int id,
                                           @RequestParam(name="playerName") String playerName) {
        if (gameService.killPlayer(gameCode, id,playerName))
            return ResponseEntity.ok().build();

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(path = "/{gameCode}/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteGame(@PathVariable(name = "gameCode") String gameCode,
                              @PathVariable(name="id") int id) {
        if (gameService.endGame(gameCode, id))
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().build();
    }

    @RequestMapping(path = "/{gameCode}", method = RequestMethod.GET)
    public ResponseEntity<PlayerAndRoleList> getPlayersAndRoles(@PathVariable(name = "gameCode") String gameCode){
        logger.info("tried to get game");

        if (!gameService.validCode(gameCode))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(gameService.getPlayerAndRoleList(gameCode));
    }

}
