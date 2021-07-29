package net.verany.api.language;

import net.verany.api.AbstractVerany;
import net.verany.api.Verany;
import net.verany.api.skull.ISkullBuilder;
import net.verany.api.skull.SkullBuilder;
import org.bson.Document;

import java.util.Locale;
import java.util.Optional;

public class LanguageWrapper extends AbstractLanguage {

    public LanguageWrapper(String name, Locale locale, String skullValue) {
        this(name, locale, skullValue, true);
    }

    public LanguageWrapper(String name, Locale locale, String skullValue, boolean enabled) {
        super(name, locale.toLanguageTag(), skullValue, enabled);
        if (getLanguage(name).isEmpty())
            Verany.LANGUAGES.add(this);
    }

    public static Optional<AbstractLanguage> getLanguage(String name) {
        return Verany.LANGUAGES.stream().filter(abstractLanguage -> abstractLanguage.getName().equalsIgnoreCase(name)).findFirst();
    }

    public static AbstractLanguage getLanguage(Document document) {
        String name = document.getString("name");
        String locale = document.getString("locale");
        String skullValue = document.getString("skullValue");
        boolean enabled = document.getBoolean("enabled");
        return new LanguageWrapper(name, Locale.forLanguageTag(locale), skullValue, enabled);
    }

    public ISkullBuilder getAsSkull() {
        return new SkullBuilder(getSkullValue());
    }
}
