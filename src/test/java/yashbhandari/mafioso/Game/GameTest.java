package yashbhandari.mafioso.Game;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void addPlayer() {
        String[] roles = {"Role1", "Role2"};
        Game game = new Game(roles);
        assertNotEquals(-1, game.addPlayer("First"));
        assertEquals(-1, game.addPlayer("First"));
        assertNotEquals(-1, game.addPlayer("Second"));
        assertEquals(-1, game.addPlayer("Third"));
    }

    @Test
    public void getRoles() {
        String[] roles = {"Role1", "Role2"};
        Game game = new Game(roles);
        int hostId = game.getHostId();
        assertTrue(game.getRoles(hostId).size() == roles.length);

        int playerId = game.addPlayer("player");
        assertTrue(game.getRoles(playerId).size() == 1);
    }
}