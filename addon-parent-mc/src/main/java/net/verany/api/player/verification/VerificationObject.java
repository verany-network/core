package net.verany.api.player.verification;

import lombok.Getter;
import lombok.Setter;
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

    private UUID key;

    public VerificationObject(VeranyProject project) {
        super(project, "players", "verification");
    }

    @Override
    public void load(UUID key) {
        this.key = key;
        load();
    }

    @Override
    public void update() {
        if (getData(VerificationMap.class) != null)
            save("verification_player");
        load();
    }

    private void load() {
        load(new LoadInfo<>("verification_player", VerificationMap.class, new VerificationMap(key, new HashMap<>())));
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

        getData(VerificationMap.class).getVerificationDataMap().put(type, data);
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
        return getData(VerificationMap.class).getVerificationDataMap().containsKey(type);
    }

    @Override
    public void createVerification(VerificationType type, VerificationData data) {
        if (isVerified(type)) return;
        if (requestedVerification(type)) return;
        getData(VerificationMap.class).getVerificationDataMap().put(type, data);
        update();
    }

    @Override
    public VerificationData getVerificationData(VerificationType type) {
        return getData(VerificationMap.class).getVerificationDataMap().get(type);
    }

    @Override
    public void unlink(VerificationType type) {
        getData(VerificationMap.class).getVerificationDataMap().remove(type);
        update();
        Verany.MESSENGER.sendMessage("TeamSpeakBot", new JSONObject().put("teamspeak", "unlink").put("uuid", key.toString()), object -> {});
    }

    @Getter
    public static class VerificationMap extends DatabaseLoadObject {

        private final Map<VerificationType, VerificationData> verificationDataMap;

        public VerificationMap(UUID uuid, Map<VerificationType, VerificationData> verificationDataMap) {
            super(uuid.toString());
            this.verificationDataMap = verificationDataMap;
        }
    }
}
