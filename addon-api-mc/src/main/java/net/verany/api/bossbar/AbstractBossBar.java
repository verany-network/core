package net.verany.api.bossbar;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

@Getter
@Setter
public abstract class AbstractBossBar {

    private final String text;
    private final long created;
    private BarColor barColor = BarColor.BLUE;
    private BarStyle barStyle = BarStyle.SOLID;
    private float progress = 1F;
    private long time;
    private String extra = "";

    public AbstractBossBar(String text, long time) {
        this.text = text;
        this.time = time;
        this.created = System.currentTimeMillis();
    }
}
