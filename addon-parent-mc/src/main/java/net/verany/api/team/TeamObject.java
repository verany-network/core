package net.verany.api.team;

import lombok.Getter;

import java.util.*;

@Getter
public class TeamObject<T extends AbstractGameTeam> implements ITeamObject<T> {

    private final List<T> teams = new ArrayList<>();
    private final Map<UUID, T> team = new HashMap<>();

    private final int maxTeams, maxPlayersInTeam;

    public TeamObject(int maxTeams, int maxPlayersInTeam) {
        this.maxTeams = maxTeams;
        this.maxPlayersInTeam = maxPlayersInTeam;
    }

    @Override
    public void loadTeams(List<T> teams) {
        this.teams.addAll(teams);
    }

    @Override
    public void loadDefaultTeams() {
        for (int i = 0; i < Math.min(VeranyGameTeam.TEAMS.size(), maxTeams); i++) {
            teams.add((T) VeranyGameTeam.TEAMS.get(i));
        }
    }

    @Override
    public T getTeam(UUID uuid) {
        return team.getOrDefault(uuid, null);
    }

    @Override
    public void removePlayersTeam(UUID uuid) {
        if (getTeam(uuid) == null) return;
        team.remove(uuid);
    }

    @Override
    public List<UUID> getPlayersInTeam(T team) {
        List<UUID> toReturn = new ArrayList<>();
        this.team.forEach((uuid, team1) -> {
            if (team1.getName().equals(team.getName()))
                toReturn.add(uuid);
        });
        return toReturn;
    }

    @Override
    public void addPlayerToTeam(UUID uuid, T team) {
        removePlayersTeam(uuid);
        this.team.put(uuid, team);
        System.out.println("pit: " + getPlayersInTeam(team).size() + " ~ t: " + team.getName());
    }

    @Override
    public T getTeamByName(String name) {
        return teams.stream().filter(searched -> searched.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public T getRandomFreeTeam() {
        T random = teams.get(new Random().nextInt(teams.size()));
        if (random == null) return null;
        int playersInTeam = getPlayersInTeam(random).size();
        System.out.println("pit: " + playersInTeam + " ~ mpit: " + maxPlayersInTeam + " ~ t: " + random.getName());
        if (playersInTeam >= maxPlayersInTeam)
            return getRandomFreeTeam();
        return random;
    }

    @Override
    public boolean isTeamEquals(UUID uuid, T team) {
        if (getTeam(uuid) == null) return false;
        return getTeam(uuid).getName().equals(team.getName());
    }

}
