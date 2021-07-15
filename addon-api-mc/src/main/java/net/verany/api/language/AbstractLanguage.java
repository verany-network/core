package net.verany.api.language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class AbstractLanguage {

    public static final List<AbstractLanguage> LANGUAGES = new ArrayList<>();

    private final String name;
    private final String languageTag;
    private boolean enabled = true;

    public Locale getLocale() {
        return Locale.forLanguageTag(languageTag);
    }

}
