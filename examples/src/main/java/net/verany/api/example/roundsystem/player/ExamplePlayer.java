package net.verany.api.example.roundsystem.player;

import net.verany.api.Verany;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.scoreboard.IScoreboardBuilder;
import net.verany.api.scoreboard.ScoreboardBuilder;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ExamplePlayer extends DatabaseLoader implements IExamplePlayer {

    private UUID uniqueId;
    private IPlayerInfo playerInfo;
    private Player player;

    public ExamplePlayer(VeranyProject project) {
        super(project, "collection", "database");
    }

    @Override
    public void load(UUID key) {
        uniqueId = key;
        playerInfo = Verany.getPlayer(key);
        player = playerInfo.getPlayer();

        // Load Object from Database
        load(new LoadInfo<>("player", ExamplePlayerObject.class, new ExamplePlayerObject(key.toString(), "Max Mustermann", 21)));
    }

    @Override
    public void update() {
        // Update Object to Database
        save("player");
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public void setScoreboard() {
        IScoreboardBuilder scoreboardBuilder = new ScoreboardBuilder(player);
        scoreboardBuilder.setTitle("Scoreboard Title");
        scoreboardBuilder.setSlot(2, " ");
        scoreboardBuilder.setSlot(1, "Scoreboard Text");
        scoreboardBuilder.setSlot(0, " ");
    }

    @Override
    public void setName(String name) {
        if (getDataOptional(ExamplePlayerObject.class).isEmpty()) return;
        getDataOptional(ExamplePlayerObject.class).get().setName(name);
    }

    @Override
    public void setAge(int age) {
        if (getDataOptional(ExamplePlayerObject.class).isEmpty()) return;
        getDataOptional(ExamplePlayerObject.class).get().setAge(age);
    }

    @Override
    public String getName() {
        if (getDataOptional(ExamplePlayerObject.class).isEmpty()) return null;
        return getDataOptional(ExamplePlayerObject.class).get().getName();
    }

    @Override
    public int getAge() {
        if (getDataOptional(ExamplePlayerObject.class).isEmpty()) return -1;
        return getDataOptional(ExamplePlayerObject.class).get().getAge();
    }

}
