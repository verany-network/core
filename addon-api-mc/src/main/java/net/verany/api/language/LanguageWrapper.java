package net.verany.api.language;

import org.bson.Document;

import java.util.Locale;

public class LanguageWrapper extends AbstractLanguage {

    public LanguageWrapper(String name, Locale locale) {
        this(name, locale, true);
    }

    public LanguageWrapper(String name, Locale locale, boolean enabled) {
        super(name, locale.toLanguageTag(), enabled);
        LANGUAGES.add(this);
    }

    public static AbstractLanguage getLanguage(String name) {
        return LANGUAGES.stream().filter(abstractLanguage -> abstractLanguage.getName().equalsIgnoreCase(name)).findFirst().orElse(VeranyLanguage.ENGLISH);
    }

    public static AbstractLanguage getLanguage(Document document) {
        String name = document.getString("name");
        String locale = document.getString("locale");
        boolean enabled = document.getBoolean("enabled");
        return new LanguageWrapper(name, Locale.forLanguageTag(locale), enabled);
    }
}
