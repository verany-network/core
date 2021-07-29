package net.verany.api.chat.request;

import lombok.Getter;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IVeranyPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class ChatRequestCallback {

    private final List<FinishType> onlyReset = new ArrayList<>(Arrays.asList(FinishType.values()));

    public abstract void accept(FinishType onFinish);

    public void reset(VeranyProject plugin, IVeranyPlayer player) {
        player.removeMetadata("chat.request");
    }

    public ChatRequestCallback onlyResetOn(FinishType... types) {
        onlyReset.addAll(Arrays.asList(types));
        return this;
    }

    @Getter
    public enum FinishType {
        SUCCESS,
        FAIL,
        TIME_OUT,
        CANCELLED;

        private String message = null;

        public FinishType message(String message) {
            this.message = message;
            return this;
        }
    }

}
