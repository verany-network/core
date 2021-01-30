package net.verany.api.chat.request;

public class ExitRequest extends ChatRequest<String> {

    public ExitRequest(long waitMillis) {
        super(new String[]{}, waitMillis);
    }
}
