package net.verany.api.achievements;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VeranyAchievementPassData {

    private final VeranyAchievement achievement;
    private final long timestamp;

}
