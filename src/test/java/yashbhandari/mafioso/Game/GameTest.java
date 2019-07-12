package yashbhandari.mafioso.Game;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {

    private static final String[] roles = {"Role1", "Role2"};

    @Test
    public void addPlayer() {
        Game game = new Game(roles);
        int hostId = game.getHostId();
        assertNotEquals("First player not added", -1, game.addPlayer("First"));
        assertTrue( "Can't find player that has been added", game.getPlayerNames().contains("First"));
        assertEquals("Duplicate player added",-1, game.addPlayer("First"));
        assertNotEquals("Second player not added",-1, game.addPlayer("Second"));
        assertEquals("Added more players than roles", -1, game.addPlayer("Third"));
        assertTrue("Added players are not alive", game.getRoles(hostId).stream().allMatch(role -> role.isAlive()));
    }

    @Test
    public void getRoles() {
        Game game = new Game(roles);
        int hostId = game.getHostId();
        assertTrue("More roles than game created with", game.getRoles(hostId).size() == roles.length);
        int playerId = game.addPlayer("Player1");
        game.addPlayer("Player2");
        assertTrue("Returned more than one player for a particular id", game.getRoles(playerId).size() == 1);
    }

    @Test
    public void killPlayer(){
        Game game = new Game(roles);
        int hostId = game.getHostId();
        game.addPlayer("Player1");
        game.killPlayer("Player1");
        assertFalse("Killed player is alive", game.getRoles(hostId).stream()
                .filter(role -> role.getPlayerName().equals("Player1"))
                .findFirst()
                .get()
                .isAlive());
    }

}