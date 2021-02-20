Verany Minecraft Core
=============

Willkommen zur Core Einrichtung,
hier lernt ihr wie ihr die Core in euer Projekt einbezieht und wir ihr diese anschließend einbezieht.

Die Core einbeziehen
----------------------

Mit den folgenden Schritten wird es euch ermöglicht unsere Core in euer Projekt einzubeziehen. Gegeben sein muss natürlich das allgemeine Verständnis zur objektorientierten Entwicklung in Java mit der aktuellsten IntelliJ IDE.

### Installation

1. Installiere dir die aktuellste Core in unserem **[Team Control Panel](https://tcp.verany.net/)** indem du im Navigationsbereich links den Navigationspunkt **[Dokumente](https://tcp.verany.net/team/documents)** aufrufst. Hier suchst du im Suchbereich nach der ID **[H5sIB0Lr](https://tcp.verany.net/team/documents?search=H5sIB0Lr)**. Wenn du nun also im hervorgehobenen Tabelleneintrag auf "Herunterladen" klickst wird die aktuellste Core Version heruntergeladen. Alternativ kannst du dir diesen Build auch über Maven oder Gradle herunterladen.

Installation unter Maven:
```java
repositories {
    maven { url = uri("https://maven.verany.net/repo/") } // Core
}

dependencies {
    compileOnlyApi("net.verany:Verany-Core_MC:2021.2.1") // Core
}
```
```xml
<!-- Verany Core -->
<repository>
    <id>Verany</id>
    <url>https://maven.verany.net/repo/</url>
</repository>

<!-- Veranya Core API -->
<dependency>
    <groupId>net.verany</groupId>
    <artifactId>Verany-Core</artifactId>
    <version>2021.2.1</version>
</dependency>
```

2. Richte deine Main Klasse unseren Vorgaben entsprechend ein:
```java
package net.verany.project;
 
import net.verany.api.Verany;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
 
@VeranyModule(name = "Project", prefix = "TestPrefix", version = "2021.2.1", authors = {"Developer"}, user = "user", host = "verany.net", password = "password", databases = {"project"})
public class YourProject extends VeranyProject {
 
    @Override
    public void onEnable() {
        Verany.loadModule(this);
        init();
    }
 
    @Override
    public void onDisable() {
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

Zusätzliche Hinweise
----------------

Private Forks sind nicht gestattet.
