Verany Minecraft Core
=============

Willkommen zur Core Einrichtung,
hier lernt ihr wie ihr die Core in euer Projekt einbezieht und wir ihr diese anschließend einbezieht.

Die Core einbeziehen
----------------------

Mit den folgenden Schritten wird es euch ermöglicht unsere Core in euer Projekt einzubeziehen. Gegeben sein muss natürlich das allgemeine Verständnis zur objektorientierten Entwicklung in Java mit der aktuellsten IntelliJ IDE.

### Installation

1. Installiere dir die aktuellste Core in unseren **[GitHub Releases](https://github.com/verany-network/core/releases/)**.

Installation unter Gradle:
```java

dependencies {
    compileOnly files("S:/YOURPATH/addon-parent-mc/build/libs/addon-parent-mc-2021.6.1.jar")
}
```

2. Richte deine Main Klasse unseren Vorgaben entsprechend ein:
```java
package net.verany.project;
 
import net.verany.api.Verany;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
 
@VeranyModule(name = "Project", (maxRounds = 2,) prefix = "TestPrefix", version = "2021.2.1", authors = {"Developer"}, user = "user", host = "verany.net", password = "password", databases = {"project"})
public class YourProject extends VeranyProject {
 
    @Override
    public void onEnable() {
        // Connect to Database etc.
        Verany.loadModule(this);
        init();
    }
 
    @Override
    public void onDisable() {
        // Disconnect from Database
        getConnection().disconnect();
    }
 
    @Override
    public void init() {
 
    }
}
```


Lizensierung
---------------------------

Natürlich sind wir für die Arbeit an unserem Projekt dankbar, aber es muss erwähnt werden, dass alle Dateien, die hier zu finden sind, dem Verany-Projekt dienen.
Alle Beiträge unterliegen den Bestimmungen der EULA.
