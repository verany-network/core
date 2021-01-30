package net.verany.api.chat.request;

import lombok.Getter;

@Getter
public class StringChatRequest extends ChatRequest<String> {

    private final int maxArguments;

    public StringChatRequest(String[] message, long waitMillis, int maxArguments) {
        super(message, waitMillis);
        this.maxArguments = maxArguments;
    }

    public StringChatRequest(String message, long waitMillis, int maxArguments) {
        this(new String[]{message}, waitMillis, maxArguments);
    }

    public StringChatRequest(long waitMillis, int maxArguments) {
        this(new String[]{}, waitMillis, maxArguments);
    }
}
