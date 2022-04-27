package net.verany.volcano.round;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.verany.volcano.countdown.AbstractCountdown;
import net.verany.volcano.player.IVolcanoPlayer;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class AbstractGameManager {

    private final AbstractVolcanoRound round;

    public final Random random = new Random();

    public AbstractCountdown startLobby() {
        AbstractCountdown countdown = round.getSettingValue(GameSetting.LOBBY_TASK);
        if (countdown == null) return null;
        countdown.start();
        return countdown;
    }

    public abstract void update();

    public abstract void setIngame();

    public void stop(IRoundLoader roundLoader) {
        if (roundLoader == null) throw new IllegalArgumentException("RoundLoader cannot be null!");
        RoundInfo info = RoundInfo.builder().roundId(round.getId()).players(round.getAllPlayers().stream().map(IVolcanoPlayer::getUniqueId).collect(Collectors.toList())).gameMode(round.getGameMode().getName()).end(System.currentTimeMillis()).build();
        roundLoader.addRound(info);
    }

}
