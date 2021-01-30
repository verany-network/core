package net.verany.api.language;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Placeholder {
    private final String target;
    private final Object replacement;
}
