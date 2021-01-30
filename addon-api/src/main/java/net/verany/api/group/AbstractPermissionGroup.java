package net.verany.api.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class AbstractPermissionGroup {

    private String name;
    private Color color;
    private final String scoreboardId;
    private String prefix;
    private int joinPower;
    private final List<String> permissions = new ArrayList<>();

}
