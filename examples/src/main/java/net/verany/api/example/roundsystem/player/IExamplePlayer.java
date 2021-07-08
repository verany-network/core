package net.verany.api.example.roundsystem.player;

import lombok.Getter;
import lombok.Setter;
import net.verany.api.interfaces.IDefault;
import net.verany.api.loader.database.DatabaseLoadObject;

import java.util.UUID;

public interface IExamplePlayer extends IDefault<UUID> {

    void setScoreboard();

    void setName(String name);

    void setAge(int age);

    String getName();

    int getAge();

    @Getter
    @Setter
    class ExamplePlayerObject extends DatabaseLoadObject {

        private String name;
        private int age;

        public ExamplePlayerObject(String uuid, String name, int age) {
            super(uuid);
            this.name = name;
            this.age = age;
        }
    }

}
