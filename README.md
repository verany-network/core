Verany Core
=============

Willkommen zur Core Einrichtung,
hier lernt ihr wie ihr die Core in euer Projekt einbezieht und wie ihr diese anschließend selbst bearbeiten könnt.

> Achtung: Einige Inhalte dieser Datei sind noch nicht final umgesetzt und werden erst demnächst hinzugefügt. Sollte etwas nicht richtig funktionieren wende dich bitte an eine Development-Team Leitung. Beispielsweise sollen die Verbindungsdaten und Informationen im VeranyModule in eine JSON auf dem Server ausgelagert werden.

### Installation unter Maven

Füge das zu deiner pom.xml hinzu:
```java
<repository>
    <id>verany</id>
    <url>https://verany.net/repo/repository/maven-public/</url>
</repository>
```

Und füg dann die Dependency hinzu:
```java
<dependency>
    <groupId>net.verany.core</groupId>
    <artifactId>core-api</artifactId>
    <version>1.17-R0.1-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

### Installation unter Gradle

Füge das zu deiner build.gradle hinzu:
```java
repositories {
    maven { url "https://verany.net/repo/repository/maven-public/" }
}
```
dann die Dependency:
```java
dependencies {
    compileOnly "net.verany.core:core-api:1.17-R0.1-SNAPSHOT"
}
```

Beispiel zur Verwendung
=============
```java
package net.verany.api.example;

import net.verany.api.Verany;
import net.verany.api.example.commands.ExampleCommand;
import net.verany.api.example.listener.PlayerJoinListener;
import net.verany.api.example.listener.PlayerQuitListener;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

@VeranyModule(name = "Example", prefix = "Example", version = "2021.6.1", authors = {"tylix"})
public class ExamplePlugin extends VeranyProject {

    public static ExamplePlugin INSTANCE;

    public ExamplePlugin() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        Verany.loadModule(this);

        init();
    }

    @Override
    public void init() {

        initCommands();
        initListener();
    }

    private void initCommands() {
        new ExampleCommand(this);
    }

    private void initListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
```

### Weitere Beispiele
findest du unter [Core Beispiele](https://github.com/verany-network/core/discussions/12).


Lizensierung
---------------------------

Natürlich sind wir für die Arbeit an unserem Projekt dankbar, aber es muss erwähnt werden, dass alle Dateien, die hier zu finden sind, dem Verany-Projekt dienen.
Alle Beiträge unterliegen den Bestimmungen der EULA.
