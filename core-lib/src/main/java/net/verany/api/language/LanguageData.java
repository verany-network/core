package net.verany.api.language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LanguageData {

    private final AbstractLanguage language;
    private final String message;

}
