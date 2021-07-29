package net.verany.api.prefix;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public abstract class AbstractPrefixPattern {

    public static final List<AbstractPrefixPattern> VALUES = new ArrayList<>();

    private final String key;
    private final String pattern;
    private final PrefixColor color;

    public abstract String getExample();

}
