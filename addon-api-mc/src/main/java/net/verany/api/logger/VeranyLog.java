package net.verany.api.logger;

import lombok.AllArgsConstructor;
import net.verany.api.AbstractVerany;
import net.verany.api.module.VeranyModule;

@AllArgsConstructor
public class VeranyLog {

    public static void info(VeranyModule module, String message) {
        System.out.println(AbstractVerany.format("[INFO: {0}] {1}", module.name(), message));
    }

    public static void error(VeranyModule module, String message) {
        System.out.println(AbstractVerany.format("[ERROR: {0}] {1}", module.name(), message));
    }

    public static void debug(VeranyModule module, String message) {
        System.out.println(AbstractVerany.format("[DEBUG: {0}] {1}", module.name(), message));
    }
}
