package net.verany.api.player.verification;

import lombok.Getter;
import net.verany.api.Verany;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.verifictation.IVerificationObject;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
public class VerificationObject extends DatabaseLoader implements IVerificationObject {

    private UUID uniqueId;

    public VerificationObject(VeranyProject project) {
        super(project, "players", "verification");
    }

    @Override
    public void load(UUID key) {
        this.uniqueId = key;
        load();
    }

    @Override
    public void update() {
        if (getDataOptional(VerificationMap.class).isPresent())
            save("verification_player");
        load();
    }

    private void load() {
        load(new LoadInfo<>("verification_player", VerificationMap.class, new VerificationMap(uniqueId, new HashMap<>())));
    }

    @Override
    public void confirmVerification(VerificationType type) {
        if (isVerified(type)) return;
        if (!requestedVerification(type)) return;

        VerificationData data = new VerificationData();
        data.setVerified(true);
        if (type.equals(VerificationType.TEAMSPEAK)) {
            data.setKey(getVerificationData(type).getKey());
            data.setExtra(getVerificationData(type).getExtra());
        }

        getDataOptional(VerificationMap.class).ifPresent(verificationMap -> verificationMap.getVerificationDataMap().put(type, data));
        update();
    }

    @Override
    public boolean requestedVerification(VerificationType type) {
        VerificationData data = getVerificationData(type);
        if (data == null || data.getTimestamp() + TimeUnit.MINUTES.toMillis(10) < System.currentTimeMillis())
            return false;
        return data.getKey() != null;
    }

    @Override
    public boolean isVerified(VerificationType type) {
        remove(getInfo(VerificationMap.class));
        load();
        return contains(type) && getVerificationData(type).isVerified();
    }

    private boolean contains(VerificationType type) {
        if (getDataOptional(VerificationMap.class).isEmpty()) return false;
        return getDataOptional(VerificationMap.class).get().getVerificationDataMap().containsKey(type);
    }

    @Override
    public void createVerification(VerificationType type, VerificationData data) {
        if (isVerified(type)) return;
        if (requestedVerification(type)) return;
        getDataOptional(VerificationMap.class).ifPresent(verificationMap -> verificationMap.getVerificationDataMap().put(type, data));
        update();
    }

    @Override
    public VerificationData getVerificationData(VerificationType type) {
        if (getDataOptional(VerificationMap.class).isEmpty()) return null;
        return getDataOptional(VerificationMap.class).get().getVerificationDataMap().get(type);
    }

    @Override
    public void unlink(VerificationType type) {
        getDataOptional(VerificationMap.class).get().getVerificationDataMap().remove(type);
        update();
        Verany.MESSENGER.sendMessage("TeamSpeakBot", new JSONObject().put(type.name().toLowerCase(), "unlink").put("uuid", uniqueId.toString()), object -> {
        });
    }

    @Getter
    public static class VerificationMap extends DatabaseLoadObject {

        private final Map<VerificationType, VerificationData> verificationDataMap;

        public VerificationMap(UUID uuid, Map<VerificationType, VerificationData> verificationDataMap) {
            super(uuid);
            this.verificationDataMap = verificationDataMap;
        }
    }
}
