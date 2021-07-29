package net.verany.api.message;

import net.verany.api.placeholder.Placeholder;
import net.verany.api.prefix.AbstractPrefixPattern;

public record KeyBuilder(String prefix, String key, char regex,
                         AbstractPrefixPattern prefixPattern,
                         Placeholder[] placeholders) {
}
