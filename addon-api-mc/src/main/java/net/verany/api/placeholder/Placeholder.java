package net.verany.api.placeholder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Placeholder {
    private final String target;
    private final Object replacement;
}
