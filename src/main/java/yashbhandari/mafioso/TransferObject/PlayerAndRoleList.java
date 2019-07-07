package yashbhandari.mafioso.TransferObject;

import java.util.List;

public class PlayerAndRoleList {
    private List<String> players;
    private List<String> roles;

    public PlayerAndRoleList(List<String> players, List<String> roles) {
        this.players = players;
        this.roles = roles;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
