package net.verany.api.player.party.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.verany.api.Verany;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.party.IParty;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Getter
@Setter
public class Party implements IParty {

    private final String id = Verany.generate(10);
    private UUID leader;
    private final List<UUID> member = new ArrayList<>();
    private final Map<UUID, Long> invited = new HashMap<>();
    private final long timestamp = System.currentTimeMillis();

    private final VeranyProject project;

    @Override
    public void invite(UUID uuid) {
        if (isInvited(uuid)) return;
        invited.put(uuid, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1));
        getLeader().sendMessage("you invited " + Bukkit.getPlayer(uuid).getName() + " to your party.");
    }

    @Override
    public boolean isInvited(UUID uuid) {
        return invited.containsKey(uuid);
    }

    @Override
    public boolean isInvitationExpired(UUID uuid) {
        boolean expired = invited.get(uuid) < System.currentTimeMillis();
        if (expired)
            invited.remove(uuid);
        return expired;
    }

    @Override
    public void join(UUID uuid) {
        member.add(uuid);
        invited.remove(uuid);

        Player target = Bukkit.getPlayer(uuid);
        getMember().forEach(player -> {
            player.sendMessage(target.getName() + " has entered the party.");
        });
        getLeader().sendMessage(target.getName() + " has entered your party.");
        /*if (getLeader().hasMetadata("party.timeout"))
            MoonLight.INSTANCE.removeMetadata(getLeader(), "party.timeout");*/
    }

    @Override
    public void leave(UUID uuid) {
        member.remove(uuid);

        Player target = Bukkit.getPlayer(uuid);
        getMember().forEach(player -> {
            player.sendMessage(target.getName() + " has left the party.");
        });
        getLeader().sendMessage(target.getName() + " has left your party.");
    }

    private void checkParty() {
        if (member.isEmpty()) {
            //MoonLight.INSTANCE.setMetadata(getLeader(), "party.timeout", System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3));
        }
    }

    @Override
    public boolean isLeader(UUID uuid) {
        return leader.equals(uuid);
    }

    @Override
    public Player getLeader() {
        return Bukkit.getPlayer(leader);
    }

    @Override
    public List<Player> getMember() {
        List<Player> toReturn = new ArrayList<>();
        for (UUID uuid : member)
            toReturn.add(Bukkit.getPlayer(uuid));
        return toReturn;
    }

    @Override
    public List<Player> getPlayers() {
        List<Player> toReturn = new ArrayList<>();
        for (UUID uuid : member)
            toReturn.add(Bukkit.getPlayer(uuid));
        toReturn.add(Bukkit.getPlayer(leader));
        return toReturn;
    }

}
