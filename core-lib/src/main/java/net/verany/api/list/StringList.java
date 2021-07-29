package net.verany.api.list;

import java.util.ArrayList;
import java.util.List;

public class StringList extends ArrayList<String> {

    public static StringList getNewList(String... strings) {
        StringList stringList = new StringList();
        stringList.addAll(stringList);
        return stringList;
    }

}
