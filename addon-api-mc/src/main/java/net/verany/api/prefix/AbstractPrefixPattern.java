package net.verany.api.prefix;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class AbstractPrefixPattern {

    private final String key;
    private final String pattern;
    private final PrefixColor color;

    public abstract String getExample();

}
