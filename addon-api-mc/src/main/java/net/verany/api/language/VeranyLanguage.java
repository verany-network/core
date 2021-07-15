package net.verany.api.language;

import java.util.Locale;

public class VeranyLanguage {

    public VeranyLanguage() {
    }

    public static final AbstractLanguage ENGLISH = new LanguageWrapper("english", Locale.ENGLISH);
    public static final AbstractLanguage GERMAN = new LanguageWrapper("german", Locale.GERMAN);
    public static final AbstractLanguage SPANISH = new LanguageWrapper("spanish", new Locale("es", "ES"));

    public static void load() {}

}
