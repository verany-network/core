package net.verany.api.language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.verany.api.skull.ISkullBuilder;

import java.util.Locale;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class AbstractLanguage {

    private final String name;
    private final String languageTag;
    private final String skullValue;
    private boolean enabled = true;

    public abstract ISkullBuilder getAsSkull();

    public Locale getLocale() {
        return Locale.forLanguageTag(languageTag);
    }

}
