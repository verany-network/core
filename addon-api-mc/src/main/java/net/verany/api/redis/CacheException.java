package net.verany.api.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CacheException extends Throwable {

    private final String message;

}
