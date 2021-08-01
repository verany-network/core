package net.verany.api.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@Deprecated
public abstract class AbstractTask {

    private final long waitTime;
    private long lastUpdate;

    public abstract void run();

}
