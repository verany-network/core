package net.verany.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.event.IEventManager;
import net.verany.api.interfaces.IDefault;
import net.verany.api.loader.Loader;
import net.verany.api.messaging.VeranyMessenger;
import net.verany.api.task.AbstractTask;
import net.verany.api.task.MainTask;
import org.ocpsoft.prettytime.PrettyTime;

@Getter
public abstract class AbstractVerany {

    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final JsonParser PARSER = new JsonParser();

    public static final List<Loader> LOADERS = new ArrayList<>();
    public static final List<AbstractTask> TASKS = new ArrayList<>();
    public static final List<PlayerLoaderData<?>> PLAYER_LOADER_DATA = new ArrayList<>();
    public static VeranyMessenger messenger;
    public static IEventManager eventManager;
    private static MainTask mainTask;
    public static final String KEY = generate(10);
    public static boolean loaded = false;

    public static void addTask(AbstractTask... tasks) {
        TASKS.addAll(Arrays.asList(tasks));
    }

    public static void removeTask(AbstractTask task) {
        TASKS.remove(task);
    }

    public static void shutdown() {
        mainTask.setRunning(false);
        LOADERS.forEach(Loader::save);
    }

    public static void load() {
        if (!loaded) {
            loaded = true;

            Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
            mongoLogger.setLevel(Level.OFF);
            mainTask = new MainTask();
            new Thread(mainTask, "core-main-thread").start();
        }
    }

    public static int getRandomNumberBetween(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static String generate(int length) {
        StringBuilder result = new StringBuilder();
        while (result.length() < length)
            result.append(getChar());
        return result.toString();
    }

    public static <T extends IDefault<?>> void setPlayer(Class<T> tClass, T player) {
        PLAYER_LOADER_DATA.add(new PlayerLoaderData<>(player.getUniqueId().toString(), tClass, player));
    }

    public static <T extends IDefault<?>> T getPlayer(String key, Class<T> tClass) {
        PlayerLoaderData<T> loaderData = getLoadData(key, tClass);
        if (loaderData == null) return null;
        return loaderData.getPlayer();
    }

    public static <T extends IDefault<?>> List<T> getPlayers(Class<T> tClass) {
        List<T> toReturn = new ArrayList<>();
        for (PlayerLoaderData<?> playerLoaderDatum : PLAYER_LOADER_DATA)
            if (playerLoaderDatum.getTClass().equals(tClass))
                toReturn.add((T) playerLoaderDatum.getPlayer());
        return toReturn;
    }

    public static <T extends IDefault<?>> void removePlayer(String key, Class<T> tClass) {
        PLAYER_LOADER_DATA.remove(getLoadData(key, tClass));
    }

    private static <T extends IDefault<?>> PlayerLoaderData<T> getLoadData(String key, Class<T> tClass) {
        PlayerLoaderData<T> toReturn = null;
        for (PlayerLoaderData<?> playerLoaderDatum : PLAYER_LOADER_DATA)
            if (playerLoaderDatum.getTClass().equals(tClass) && playerLoaderDatum.getKey().equalsIgnoreCase(key))
                toReturn = (PlayerLoaderData<T>) playerLoaderDatum;
        return toReturn;
    }

    private static char getChar() {
        int s = getInt("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length());
        return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(s - 1);
    }

    private static int getInt(int max) {
        return (int) Math.ceil(Math.random() * max);
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

    public static String intToRoman(int num) {
        StringBuilder sb = new StringBuilder();
        int times;
        String[] romans = new String[]{"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};
        int[] ints = new int[]{1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};
        for (int i = ints.length - 1; i >= 0; i--) {
            times = num / ints[i];
            num %= ints[i];
            while (times > 0) {
                sb.append(romans[i]);
                times--;
            }
        }
        return sb.toString();
    }

    public static String format(double number) {
        String r = new DecimalFormat("##0E0").format(number);
        r = r.replaceAll("E[0-9]", new String[]{"", "K", "M", "B", "T"}[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
        int MAX_LENGTH = 4;
        while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]"))
            r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
        return r;
    }

    public static int getIdFromEnum(Class<?> enumClass, Object enumObject) {
        int toReturn = -1;
        Object[] possibleValues = enumClass.getEnumConstants();
        for (int i = 0; i < possibleValues.length; i++) {
            if (possibleValues[i].equals(enumObject)) {
                toReturn = i;
                break;
            }
        }
        return toReturn;
    }

    public static String getNextEnumValue(Class<?> enumClass, Object enumObject) {
        Object[] possibleValues = enumClass.getEnumConstants();
        int enumSize = possibleValues.length - 1;
        int currentId = getIdFromEnum(enumClass, enumObject);
        for (Object possibleValue : possibleValues) {
            int newId = getIdFromEnum(enumClass, possibleValue);
            if (newId == currentId) {
                currentId = newId + 1;
                break;
            }
        }
        if (currentId > enumSize)
            currentId = 0;
        return possibleValues[currentId].toString();
    }

    public static String getPreviousEnumValue(Class<?> enumClass, Object enumObject) {
        Object[] possibleValues = enumClass.getEnumConstants();
        int enumSize = possibleValues.length - 1;
        int currentId = getIdFromEnum(enumClass, enumObject);
        for (Object possibleValue : possibleValues) {
            int newId = getIdFromEnum(enumClass, possibleValue);
            if (newId == currentId) {
                currentId = newId - 1;
                break;
            }
        }
        if (currentId < 0)
            currentId = enumSize;
        return possibleValues[currentId].toString();
    }

    public static String format(String message, Object... arguments) {
        return MessageFormat.format(message, arguments);
    }

    public static String asDecimal(Object o) {
        return new DecimalFormat().format(o).replace(",", ".");
    }

    /*public static <T> List<T> sortList(Class<T> tClass, List<T> list, boolean reverse) {
        if (tClass.equals(Integer.class)) {
            List<Integer> integers = (List<Integer>) list;
            Collections.sort(integers);
            if (reverse)
                Collections.reverse(integers);
            return (List<T>) integers;
        }
        if (tClass.equals(Long.class)) {
            List<Long> integers = (List<Long>) list;
            Collections.sort(integers);
            if (reverse)
                Collections.reverse(integers);
            return (List<T>) integers;
        }
        if (tClass.equals(String.class)) {
            List<String> integers = (List<String>) list;
            Collections.sort(integers);
            if (reverse)
                Collections.reverse(integers);
            return (List<T>) integers;
        }
        return new ArrayList<>();
    }*/

    public static List<String> getShortList(List<String> list, int amount) {
        List<String> toReturn = new ArrayList<>();
        int c = 1;
        for (String s : list) {
            toReturn.add("§8- §e" + s);
            if (c == amount) {
                if (list.size() > c) {
                    int left = list.size() - c;
                    toReturn.add("§7§o+" + left + " more§8...");
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

    @AllArgsConstructor
    @Getter
    public static class PlayerLoaderData<T extends IDefault<?>> {
        private final String key;
        private final Class<T> tClass;
        private final T player;
    }


}
