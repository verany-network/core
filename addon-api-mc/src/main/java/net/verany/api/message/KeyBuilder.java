package net.verany.api.message;

import lombok.Builder;
import lombok.Getter;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.prefix.AbstractPrefixPattern;

@Builder
@Getter
public class KeyBuilder {
    private final String prefix;
    private final String key;
    private final char regex;
    private final AbstractPrefixPattern prefixPattern;
    private final Placeholder[] placeholders;

}
