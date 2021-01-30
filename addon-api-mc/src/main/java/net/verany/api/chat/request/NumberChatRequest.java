package net.verany.api.chat.request;

import lombok.Getter;

@Getter
public class NumberChatRequest extends ChatRequest<Integer> {

    private final int min, max;

    public NumberChatRequest(String[] message, long waitMillis, int min, int max) {
        super(message, waitMillis);
        this.min = min;
        this.max = max;
    }

    public NumberChatRequest(String message, long waitMillis, int min, int max) {
        this(new String[]{message}, waitMillis, min, max);
    }

    public NumberChatRequest(long waitMillis, int min, int max) {
        this(new String[]{}, waitMillis, min, max);
    }
}
