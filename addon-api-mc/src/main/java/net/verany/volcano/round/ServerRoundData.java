package net.verany.volcano.round;

import org.bson.Document;

import java.util.List;

public record ServerRoundData(List<Document> documents) {}