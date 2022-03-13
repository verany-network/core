package net.verany.test;

import net.verany.api.Verany;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.player.IVeranyPlayer;
import net.verany.api.player.ProfileObject;
import net.verany.test.player.TestPlayer;
import org.bson.Document;

import java.util.UUID;

@VeranyModule(name = "TestProject", prefix = "Test", version = "2022.3.1", authors = {"tylix"}, parentConnectionPath = "%name%")
public class TestProject extends VeranyPlugin {

    @Override
    public void enable() {
        Verany.loadModule(this, this::init);
    }

    @Override
    public void init() {
        loadPlayers();
        System.out.println("loaded");
    }

    private void loadPlayers() {
        Verany.PROFILE_OBJECT = new ProfileObject();

        for (Document players : getConnection().getCollection("players").find()) {
            IVeranyPlayer playerInfo = new TestPlayer(this, players.getString("name"));
            playerInfo.load(UUID.fromString(players.getString("uuid")));
            Verany.setPlayer(IVeranyPlayer.class, playerInfo);
            Verany.PROFILE_OBJECT.setPlayer(IVeranyPlayer.class, playerInfo);

            System.out.println(playerInfo.getName());
        }
    }

    @Override
    public void disable() {

    }
}
