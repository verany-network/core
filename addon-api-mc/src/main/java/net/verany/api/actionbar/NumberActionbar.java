package net.verany.api.actionbar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumberActionbar extends AbstractActionbar {
    private int amount;

    public NumberActionbar(String text, long time, int amount) {
        super(text, time);
        this.amount = amount;
    }
}