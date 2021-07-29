package net.verany.api.update.date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.AbstractVerany;

import java.util.Deque;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class UpdateEntry {

    private final String id = AbstractVerany.generateString(10);
    private final String author;
    private final String title = "unknown";
    private final String topic;
    private final Deque<String> pages;
    private final UUID creator;
    private final long timestamp = System.currentTimeMillis();
    private final long publishDate;

    public boolean isPublished() {
        return System.currentTimeMillis() >= publishDate;
    }

}
