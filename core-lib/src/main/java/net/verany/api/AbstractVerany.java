package net.verany.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import net.verany.api.adapter.InterfaceAdapter;
import net.verany.api.enumhelper.EnumHelper;
import net.verany.api.gamemode.AbstractGameMode;
import net.verany.api.interfaces.IDefault;
import net.verany.api.language.AbstractLanguage;
import net.verany.api.language.LanguageData;
import net.verany.api.loader.Loader;
import net.verany.api.message.MessageData;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IProfileObject;
import net.verany.api.player.IVeranyPlayer;
import net.verany.api.player.permission.group.AbstractPermissionGroup;
import net.verany.api.prefix.AbstractPrefixPattern;
import net.verany.api.task.AbstractTask;
import net.verany.api.task.MainTask;
import net.verany.api.websocket.VeranyMessenger;
import net.verany.volcano.countdown.AbstractCountdown;
import org.bson.Document;
import org.ocpsoft.prettytime.PrettyTime;

import java.net.URI;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public abstract class AbstractVerany {

    public static Gson GSON = new GsonBuilder().registerTypeAdapter(AbstractLanguage.class, new InterfaceAdapter<AbstractLanguage>()).registerTypeAdapter(AbstractGameMode.class, new InterfaceAdapter<AbstractGameMode>()).registerTypeAdapter(AbstractPermissionGroup.class, new InterfaceAdapter<AbstractPermissionGroup>()).setPrettyPrinting().create();

    public static final List<Loader> LOADERS = new ArrayList<>();
    public static final List<AbstractLanguage> LANGUAGES = new CopyOnWriteArrayList<>();
    public static final List<MessageData> MESSAGES = new CopyOnWriteArrayList<>();
    public static final List<AbstractCountdown> COUNTDOWNS = new CopyOnWriteArrayList<>();
    public static final List<Runnable> SOCKET_OPEN = new ArrayList<>();
    public static final List<PlayerLoaderData<?>> PLAYER_LOADER_DATA = new ArrayList<>();
    @Deprecated
    public static final List<AbstractTask> TASKS = new CopyOnWriteArrayList<>();
    public static MainTask mainTask;

    public static VeranyMessenger MESSENGER;
    public static IProfileObject PROFILE_OBJECT;

    public static final String KEY = generateString(10);

    @SneakyThrows
    public static void connectToWebsocket() {
        if (MESSENGER != null) return;
        MESSENGER = new VeranyMessenger(new URI("wss://demo.verany.net"));
        MESSENGER.setReuseAddr(true);
        MESSENGER.connect();
    }

    public static String getPrefix(String s, AbstractPrefixPattern pattern) {
        boolean hasUpperCaseInMiddle = false;
        int a = 0;

        StringBuilder first = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            if (i != 0 && Character.isUpperCase(s.charAt(i))) {
                hasUpperCaseInMiddle = true;
                break;
            } else {
                first.append(s.charAt(i));
                a++;
            }

        StringBuilder second = new StringBuilder();
        if (hasUpperCaseInMiddle)
            for (int i = a; i < s.length(); i++)
                second.append(s.charAt(i));

        return MessageFormat.format(pattern.getPattern(), first.toString(), second.toString());
    }

    public static String getPrefix(String s) {
        return getPrefix(s, AbstractPrefixPattern.VALUES.stream().findFirst().orElse(null));
    }

    public static int getRandomNumberBetween(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static String generateString(int length) {
        StringBuilder result = new StringBuilder();
        while (result.length() < length)
            result.append(getChar());
        return result.toString();
    }

    @Deprecated
    public static String generate(int length) {
        return generateString(length);
    }

    private static char getChar() {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int s = getInt(alphabet.length());
        return alphabet.charAt(s - 1);
    }

    private static int getInt(int max) {
        return (int) Math.ceil(Math.random() * max);
    }

    public static String intToRoman(int num) {
        String[] rnChars = {"M", "CM", "D", "C", "XC", "L", "X", "IX", "V", "I"};
        int[] rnVals = {1000, 900, 500, 100, 90, 50, 10, 9, 5, 1};
        StringBuilder retVal = new StringBuilder();

        for (int i = 0; i < rnVals.length; i++) {
            int numberInPlace = num / rnVals[i];
            if (numberInPlace == 0) continue;
            retVal.append(numberInPlace == 4 && i > 0 ? rnChars[i] + rnChars[i - 1] : new String(new char[numberInPlace]).replace("\0", rnChars[i]));
            num = num % rnVals[i];
        }
        return retVal.toString();
    }

    public static AbstractLanguage getLanguage(Document document) {
        String name = document.getString("name");
        String locale = document.getString("locale");
        String skullValue = document.getString("skullValue");
        boolean enabled = document.getBoolean("enabled");
        return new AbstractLanguage(name, locale, skullValue, enabled) {
        };
    }

    public static void registerGameMode(VeranyProject project, AbstractGameMode gameMode) {
        //VeranyServer.registerGameMode(project, gameMode);
    }

    public static void createMessage(VeranyProject project, String key) {
        if (project.getConnection().getCollection("network", "messages").find(Filters.eq("key", key)).first() != null) {
            System.out.println("tried to create key " + key + " but already exist!");
            return;
        }
        Document document = new Document("key", key);
        for (AbstractLanguage value : LANGUAGES)
            document.append(value.getName(), key);
        project.getConnection().getCollection("network", "messages").insertOne(document);
    }

    public static String getMessage(String key, AbstractLanguage language) {
        return MESSAGES.stream()
                .filter(messageData -> messageData.getKey().equals(key))
                .map(messageData -> {
                    Optional<LanguageData> s = messageData
                            .getLanguageDataList()
                            .stream()
                            .filter(languageData -> languageData.getLanguage().getName().equals(language.getName()))
                            .findFirst();
                    if (s.isEmpty()) return null;
                    return s.get().getMessage();
                })
                .findFirst()
                .orElse(null);
    }

    public static FindIterable<Document> getDocuments(VeranyProject project, String collection, String database, int limit, String... fieldNames) {
        return project.getConnection().getCollection(database, collection).find().sort(Sorts.descending(fieldNames)).limit(limit);
    }

    public static FindIterable<Document> getDocuments(VeranyProject project, String collection, int limit, String... fieldNames) {
        return getDocuments(project, collection, project.getConnection().getDatabaseManagers().get(0).getDatabaseName(), limit, fieldNames);
    }

    public static int getRanking(VeranyProject project, UUID uuid, String collection, String database, String... fieldNames) {
        int i = 1;

        FindIterable<Document> online = project.getConnection().getCollection(database, collection).find().sort(Sorts.descending(fieldNames));
        for (Document doc : online) {
            if (doc.getString("uuid").equals(uuid.toString()))
                break;
            i++;
        }
        return i;
    }

    public static <T> List<T> sortList(List<SortData<T>> list, boolean reverse) {
        List<T> toReturn = new ArrayList<>();
        for (SortData<T> tSortData : list.stream().sorted(Comparator.comparing(SortData::key)).collect(Collectors.toList()))
            toReturn.add(tSortData.t());
        if (reverse)
            Collections.reverse(toReturn);
        return toReturn;
    }

    public static <T> List<T> sortList(List<T> list, Comparator<T> comparator) {
        return list.stream().sorted(comparator).collect(Collectors.toList());
    }

    public static <T extends IDefault<UUID>> void setPlayer(Class<T> tClass, T player) {
        PLAYER_LOADER_DATA.add(new PlayerLoaderData<>(player.getUniqueId(), tClass, player));
    }

    public static <T extends IDefault<UUID>> T getPlayer(UUID key, Class<T> tClass) {
        PlayerLoaderData<T> loaderData = getLoadData(key, tClass);
        if (loaderData == null) return null;
        return loaderData.player();
    }

    public static <T extends IDefault<?>> List<T> getPlayers(Class<T> tClass) {
        return PLAYER_LOADER_DATA
                .stream()
                .filter(playerLoaderData ->
                        playerLoaderData.tClass().equals(tClass))
                .map(playerLoaderData ->
                        (T) playerLoaderData.player())
                .collect(Collectors.toList());
    }

    public static <T extends IDefault<?>> void removePlayer(UUID key, Class<T> tClass) {
        PLAYER_LOADER_DATA.remove(getLoadData(key, tClass));
    }

    private static <T extends IDefault<?>> PlayerLoaderData<T> getLoadData(UUID key, Class<T> tClass) {
        Optional<PlayerLoaderData<?>> loadData = PLAYER_LOADER_DATA
                .stream()
                .filter(playerLoaderData ->
                        playerLoaderData.tClass().equals(tClass) && playerLoaderData.key().equals(key))
                .findFirst();
        if (loadData.isEmpty()) return null;
        return (PlayerLoaderData<T>) loadData.get();
    }

    public static String getPrettyTime(Locale locale, long time) {
        return new PrettyTime(locale).format(new Date(time));
    }

    public static String formatSeconds(int seconds) {
        int days = seconds / 60 / 60 / 24 % 365;
        if (days > 0)
            return String.format("%02d:%02d:%02d:%02d", days, seconds / 60 / 60 % 24, (seconds % 3600) / 60, seconds % 60);
        return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    }

    @Deprecated
    public static String format(double number) {
        return shortNumber(number);
    }

    public static String shortNumber(double number) {
        String r = new DecimalFormat("##0E0").format(number);
        r = r.replaceAll("E[0-9]", new String[]{"", "K", "M", "B", "T"}[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
        int MAX_LENGTH = 4;
        while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]"))
            r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
        return r;
    }

    @Deprecated
    public static int getIdFromEnum(Class<?> enumClass, Object enumObject) {
        return EnumHelper.INSTANCE.getIdFromEnum(enumClass, enumObject);
    }

    @Deprecated
    public static String getNextEnumValue(Class<?> enumClass, Object enumObject) {
        return EnumHelper.INSTANCE.getNextEnumValue(enumClass, enumObject);
    }

    @Deprecated
    public static String getPreviousEnumValue(Class<?> enumClass, Object enumObject) {
        return EnumHelper.INSTANCE.getPreviousEnumValue(enumClass, enumObject);
    }

    public static List<String> getShortList(List<String> list, int amount, String leftText) {
        List<String> toReturn = new ArrayList<>();
        int c = 1;
        for (String s : list) {
            toReturn.add("§8- §e" + s);
            if (c == amount) {
                if (list.size() > c) {
                    int left = list.size() - c;
                    toReturn.add(leftText.replace("%left%", String.valueOf(left)));
                }
                break;
            }
            c++;
        }
        if (toReturn.isEmpty())
            toReturn.add("§8- §7/");
        return toReturn;
    }

    public static String getWithComma(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        for (String s : list) {
            count++;
            if (count == list.size())
                stringBuilder.append(s);
            else
                stringBuilder.append(s).append(", ");
        }
        return stringBuilder.toString();
    }

    public static String round(double i) {
        double rounded = i;
        rounded *= 100;
        rounded = Math.round(rounded);
        rounded /= 100;
        return String.valueOf(rounded);
    }

    public static void addCountdown(AbstractCountdown countdown) {
        COUNTDOWNS.add(countdown);
    }

    public static AbstractCountdown getCountdown(String key) {
        for (AbstractCountdown countdown : COUNTDOWNS)
            if (countdown.getKey().equalsIgnoreCase(key))
                return countdown;
        return null;
    }

    public static <T> List<T> getPageList(int page, int slots, List<T> objects) {
        List<T> toReturn = new ArrayList<>();
        int from = (page == 1 ? 0 : (page - 1) * slots);
        int to = (page == 1 ? slots : page * slots);
        for (int i = from; i < to; i++)
            if (objects.size() > i)
                toReturn.add(objects.get(i));
        return toReturn;
    }

    public static String asDecimal(Object o) {
        return new DecimalFormat().format(o).replace(",", ".");
    }


    @SneakyThrows
    private static void loadMessages(VeranyProject project) {
        System.out.println("Loading messages...");
        long current = System.currentTimeMillis();
        for (Document document : project.getConnection().getCollection("network", "messages").find()) {
            String key = document.getString("key");
            List<LanguageData> languageData = new ArrayList<>();
            for (AbstractLanguage language : LANGUAGES) {
                languageData.add(new LanguageData(language, document.getString(language.getName())));
            }
            MESSAGES.add(new MessageData(key, languageData));
        }
        System.out.println("Loading messages complete. (" + MESSAGES.size() + " - " + (System.currentTimeMillis() - current) + "ms)");
    }

    private static void loadLanguages(VeranyProject project) {
        System.out.println("Loading languages...");
        long current = System.currentTimeMillis();
        MongoCollection<Document> collection = project.getConnection().getCollection("network", "languages");
        for (AbstractLanguage language : LANGUAGES) {
            if (collection.find(Filters.eq("name", language.getName())).first() != null || !language.isEnabled())
                continue;
            String json = GSON.toJson(language);
            collection.insertOne(GSON.fromJson(json, Document.class));
        }
        for (Document document : collection.find()) {
            if (AbstractLanguage.getLanguage(document.getString("name")).isPresent() || !document.getBoolean("enabled"))
                continue;
            AbstractLanguage language = getLanguage(document);
            System.out.println("registered new language " + language.getName());
        }
        System.out.println("Loading languages complete. (" + LANGUAGES.size() + " - " + (System.currentTimeMillis() - current) + "ms)");
    }

    public static void reloadMessages(VeranyProject project) {
        MESSAGES.clear();
        loadLanguages(project);
        loadMessages(project);
    }

    public record SortData<T>(String key, T t) {
    }

    public record PlayerLoaderData<T extends IDefault<?>>(UUID key, Class<T> tClass, T player) {
    }

}
