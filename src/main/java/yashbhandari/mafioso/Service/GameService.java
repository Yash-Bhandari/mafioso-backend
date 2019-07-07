package yashbhandari.mafioso.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import yashbhandari.mafioso.Game.Game;
import yashbhandari.mafioso.Game.Role;
import yashbhandari.mafioso.TransferObject.PlayerAndRoleList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class GameService {
    private Random rand = new Random();
    private Map<String, Game> gameMap = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger(GameService.class);

    public String createGame(String... roles) {
        String gameCode = uniqueCode(3);
        gameMap.put(gameCode, new Game(roles));
        logger.info(String.format("Game created with code %s", gameCode));
        return gameCode;
    }

    public PlayerAndRoleList getPlayerAndRoleList(String gameCode){
        Game game = gameMap.get(gameCode);
        return new PlayerAndRoleList(game.getPlayerNames(), game.getRoleNames());
    }

    public int addPlayer(String gameCode, String playerName){
        return gameMap.get(gameCode).addPlayer(playerName);
    }

    //Only host and dead players see entire list
    public List<Role> getRoleList(String gameCode, int id){
        return gameMap.get(gameCode).getRoles(id);
    }

    public boolean validCode(String gameCode) {
        return gameMap.containsKey(gameCode);
    }


    private String uniqueCode(int length){

        String gameCode;
        do {
            gameCode = generateCode(length);
        } while (gameMap.containsKey(gameCode));

        return gameCode;
    }

    private String generateCode(int length) {
        String alphaNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder(length);

        rand.ints(0, alphaNum.length())
                .limit(length)
                .forEach(num -> code.append(alphaNum.charAt(num)));
        return code.toString();
    }

}
