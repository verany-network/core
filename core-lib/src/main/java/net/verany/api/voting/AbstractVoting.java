package net.verany.api.voting;

import java.util.List;
import java.util.UUID;

public interface AbstractVoting<T> {

    void prepare(T... entries);

    void prepare(List<T> entryList);

    void vote(UUID player, T value);

    void unVote(UUID player);

    boolean hasVoted(UUID player);

    boolean hasVotedForValue(UUID player, T value);

    T getResult();

    int getVotes(T value);

    List<T> getValues();

}
