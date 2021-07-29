package net.verany.api.player.leveling;

import lombok.Getter;
import net.verany.api.Verany;
import net.verany.api.player.PlayerEntry;
import net.verany.api.player.PlayerInfo;

import java.util.UUID;

@Getter
public class CreditsObject implements ICreditsObject {

    private UUID uniqueId;

    private final PlayerInfo playerInfo;

    public CreditsObject(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    @Override
    public void load(UUID key) {
        uniqueId = key;
    }

    @Override
    public void update() {
    }

    @Override
    public int getCredits() {
        return playerInfo.getData(PlayerEntry.class).getCredits();
    }

    @Override
    public String getCreditsAsDecimal() {
        return Verany.asDecimal(getCredits());
    }

    @Override
    public int setCredits(int credits) {
        playerInfo.getData(PlayerEntry.class).setCredits(credits);
        return credits;
    }

    @Override
    public int addCredits(int credits) {
        int newAmount = getCredits() + credits;
        return setCredits(newAmount);
    }

    @Override
    public int removeCredits(int credits) {
        int newAmount = getCredits() - credits;
        return setCredits(newAmount);
    }

    @Override
    public boolean hasEnoughCredits(int amount) {
        return getCredits() >= amount;
    }
}
