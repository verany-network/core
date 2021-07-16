package net.verany.api.language;

import net.verany.api.AbstractVerany;
import org.bson.Document;

import java.util.Locale;
import java.util.Optional;

public class LanguageWrapper extends AbstractLanguage {

    public LanguageWrapper(String name, Locale locale) {
        this(name, locale, true);
    }

    public LanguageWrapper(String name, Locale locale, boolean enabled) {
        super(name, locale.toLanguageTag(), enabled);
        if (getLanguage(name).isEmpty())
            AbstractVerany.LANGUAGES.add(this);
    }

    public static Optional<AbstractLanguage> getLanguage(String name) {
        return AbstractVerany.LANGUAGES.stream().filter(abstractLanguage -> abstractLanguage.getName().equalsIgnoreCase(name)).findFirst();
    }

    public static AbstractLanguage getLanguage(Document document) {
        String name = document.getString("name");
        String locale = document.getString("locale");
        boolean enabled = document.getBoolean("enabled");
        return new LanguageWrapper(name, Locale.forLanguageTag(locale), enabled);
    }
}
