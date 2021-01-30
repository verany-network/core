package net.verany.api.chat.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public abstract class ChatRequest<T> {
    private final String[] message;
    private final long waitMillis;
    private T value;
}