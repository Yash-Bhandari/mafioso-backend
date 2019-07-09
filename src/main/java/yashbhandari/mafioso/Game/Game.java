package yashbhandari.mafioso.Game;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {
    private Map<Integer, Role> roles;
    private int hostId;
    private Random rand = new Random();

    public Game(String... roles){
        this.roles = new HashMap<>();
        hostId = uniqueID();
        Stream.of(roles)
                .map(roleName -> new Role(roleName))
                .forEach(role -> this.roles.put(uniqueID(), role));
    }

    public int getHostId(){
        return hostId;
    }

    public int addPlayer(String playerName){

        if(!filled() && nameAvailable(playerName)) {
            int emptyId = findEmptyId();
            roles.get(emptyId).fillRole(playerName);
            return emptyId;
        }
        return -1;
    }

    public List<Role> getRoles(int id){
        List<Role> out = new LinkedList<>();
        if (this.hostId == id || !roles.get(id).isAlive())
            out.addAll(roles.values());
        else
            out.add(roles.get(id));
        return out;
    }

    public Role getRole(int playerId){
        return roles.get(playerId);
    }

    public List<String> getRoleNames(){
        return roles.values().stream()
                .map(role -> role.getRoleName())
                .collect(Collectors.toList());
    }

    public List<String> getPlayerNames() {
        return roles.values().stream()
                .map(role -> role.getPlayerName())
                .filter(name -> name != "")
                .collect(Collectors.toList());
    }

    public boolean filled(){
        return roles.values().stream()
                .allMatch(role -> role.isFilled());
    }

    private Integer uniqueID(){
        Random rand = new Random();
        int id;
        do {
            id = rand.nextInt(Integer.MAX_VALUE);
        }
        while(roles.containsKey(id));

        return id;
    }

    private int findEmptyId() {
        List<Integer> emptyIds = roles.entrySet().stream()
                .filter(entry -> !entry.getValue().isFilled())
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());

        return emptyIds.get(rand.nextInt(emptyIds.size()));
    }

    private boolean nameAvailable(String name) {
        return roles.values().stream()
                .noneMatch(role -> role.getPlayerName().equals(name));
    }


}
