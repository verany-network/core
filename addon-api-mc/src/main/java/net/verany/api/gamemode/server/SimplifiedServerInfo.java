package net.verany.api.gamemode.server;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SimplifiedServerInfo {
    private String group;
    private String name;
    private int id;
    private String launcher;
    private int onlineCount;
}