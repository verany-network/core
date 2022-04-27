package net.verany.volcano.round;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.Document;

import java.util.List;


@AllArgsConstructor
@Getter
public class ServerRoundData {

    private final List<Document> documents;

}