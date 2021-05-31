package net.verany.api.team;

import java.util.List;
import java.util.UUID;

public interface ITeamObject<T extends AbstractGameTeam> {

    void loadTeams(List<T> teams);

    void loadDefaultTeams();

    T getTeam(UUID uuid);

    void removePlayersTeam(UUID uuid);

    List<UUID> getPlayersInTeam(T team);

    void addPlayerToTeam(UUID uuid, T team);

    T getTeamByName(String name);

    T getRandomFreeTeam();

    List<T> getTeams();

    boolean isTeamEquals(UUID uuid, T team);

}
