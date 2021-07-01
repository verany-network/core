package net.verany.api.example.roundsystem.game;

import lombok.Getter;
import net.verany.api.team.AbstractGameTeam;
import net.verany.api.team.ITeamObject;
import net.verany.api.team.TeamObject;
import net.verany.volcano.round.AbstractGameManager;
import net.verany.volcano.round.AbstractVolcanoRound;

@Getter
public class AbstractExampleManager extends AbstractGameManager {

    private final ITeamObject<AbstractGameTeam> teamObject;

    public AbstractExampleManager(AbstractVolcanoRound round) {
        super(round);

        this.teamObject = new TeamObject<>(2, 1);
        this.teamObject.loadDefaultTeams();
    }

    @Override
    public void update() {

    }

    @Override
    public void setIngame() {

    }

    @Override
    public void reset() {

    }
}
