package net.verany.test;

import net.verany.api.module.VeranyPlugin;

public class Main {

    public static void main(String[] args) {
        VeranyPlugin plugin = new TestProject();
        plugin.enable();

        Runtime.getRuntime().addShutdownHook(new Thread(plugin::disable, "Shutdown-Hook"));
    }

}
