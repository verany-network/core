package net.verany.api.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.language.LanguageData;

import java.util.List;

@AllArgsConstructor
@Getter
public class MessageData {

    private final String key;
    private final List<LanguageData> languageDataList;

    public boolean isEdited() {
        boolean toReturn = false;
        for (LanguageData languageData : languageDataList) {
            if (!languageData.getMessage().equals(key)) {
                toReturn = true;
                break;
            }
        }
        return toReturn;
    }

}
