package net.verany.api.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ReportEntry {

    private final UUID reporter;
    private final UUID target;
    private final long timestamp = System.currentTimeMillis();

}
