package net.verany.api.language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.verany.api.AbstractVerany;
import org.bson.Document;

import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class AbstractLanguage {

    private final String name;
    private final String languageTag;
    private final String skullValue;
    private boolean enabled = true;

    public Locale getLocale() {
        return Locale.forLanguageTag(languageTag);
    }

    public static Optional<AbstractLanguage> getLanguage(String name) {
        return AbstractVerany.LANGUAGES.stream().filter(abstractLanguage -> abstractLanguage.getName().equalsIgnoreCase(name)).findFirst();
    }


}
