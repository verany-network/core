package net.verany.api.message;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractComponentBuilder {

    private HoverEvent hoverEvent;
    private ClickEvent clickEvent;
    private final String text;
    private final List<TextComponent> extras = new ArrayList<>();

    public AbstractComponentBuilder(String text) {
        this.text = text;
        onCreate();
    }

    public abstract void onCreate();

    /**
     * @param clickEvent click event of text component
     */
    public void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    /**
     * @param hoverEvent hover event of text component
     */
    public void setHoverEvent(HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

    /**
     * @param component extra text component
     */
    public void addExtra(TextComponent component) {
        extras.add(component);
    }

    /**
     * @return returns text of text component
     */
    public String getText() {
        return text;
    }

    /**
     * @return returns click event of text component
     */
    public ClickEvent getClickEvent() {
        return clickEvent;
    }

    /**
     * @return returns hover event of text compnent
     */
    public HoverEvent getHoverEvent() {
        return hoverEvent;
    }

    /**
     * @return returns list of text compnent extras
     */
    public List<TextComponent> getExtras() {
        return extras;
    }
}
