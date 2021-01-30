package net.verany.api.actionbar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractActionbar {
    private final String text;
    private final long created;
    private long time;
    private String extra = "";

    public AbstractActionbar(String text, long time) {
        this.text = text;
        this.time = time;
        this.created = System.currentTimeMillis();
    }
}