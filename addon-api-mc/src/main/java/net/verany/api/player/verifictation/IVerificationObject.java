package net.verany.api.player.verifictation;

import lombok.Getter;
import lombok.Setter;
import net.verany.api.player.IPlayerLoader;

public interface IVerificationObject extends IPlayerLoader {

    void confirmVerification(VerificationType type);

    boolean requestedVerification(VerificationType type);

    boolean isVerified(VerificationType type);

    void createVerification(VerificationType type, VerificationData data);

    VerificationData getVerificationData(VerificationType type);

    void unlink(VerificationType type);

    enum VerificationType {
        WEB,
        DISCORD,
        TEAMSPEAK
    }

    @Getter
    @Setter
    class VerificationData {

        private String key;
        private String extra;
        private boolean verified = false;
        private final long timestamp = System.currentTimeMillis();

    }

}
