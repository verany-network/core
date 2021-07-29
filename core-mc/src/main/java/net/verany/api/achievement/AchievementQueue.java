package net.verany.api.achievement;

import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.verany.api.achievements.VeranyAchievement;
import net.verany.api.message.AbstractComponentBuilder;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.module.VeranyProject;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.IPlayerInfo;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AchievementQueue {

    private final IPlayerInfo playerInfo;
    private final VeranyPlugin project;
    private final List<VeranyAchievement> queue = new ArrayList<>();
    private boolean running = false;

    public void addToQueue(VeranyAchievement achievement) {
        queue.add(achievement);
        start();
    }

    private void start() {
        if (running) return;
        running = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (queue.isEmpty()) {
                    cancel();
                    running = false;
                    return;
                }

                Player player = playerInfo.getPlayer();

                VeranyAchievement achievement = queue.get(0);
                String[] description = playerInfo.getKeyArray("achievement.description." + achievement.getName().toLowerCase(), '~');
                StringBuilder descriptionBuilder = new StringBuilder();
                int count = 1;
                for (String s : description) {
                    if (count == description.length)
                        descriptionBuilder.append(s);
                    else
                        descriptionBuilder.append(s).append("\n");
                    count++;
                }
                playerInfo.sendMessage(new AbstractComponentBuilder(playerInfo.getPrefix("VeranyCore") + playerInfo.getKey("achievement.passed", new Placeholder("%name%", playerInfo.getKey("achievement." + achievement.getName().toLowerCase())))) {
                    @Override
                    public void onCreate() {
                        setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(descriptionBuilder.toString()).create()));
                    }
                });
                playerInfo.getLevelObject().addExp(15 + (achievement.getDifficulty() * 3));
                playerInfo.getCreditsObject().addCredits(50);
                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.8F, 1.7F);

                queue.remove(achievement);
                playerInfo.update();

            }
        }.runTaskTimer(project, 0, 20 * 3);
    }

}
