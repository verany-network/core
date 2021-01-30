package net.verany.api.language;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@AllArgsConstructor
@Getter
public enum EnumLanguage {
    ENGLISH(Locale.ENGLISH),
    GERMAN(Locale.GERMAN),
    SPANISH(Locale.FRENCH);

    private final Locale locale;
}
