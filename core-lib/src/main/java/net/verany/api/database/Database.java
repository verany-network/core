package net.verany.api.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Database {
    private final String user,password,hostname, authSource;
    private final String[] databases;
}
