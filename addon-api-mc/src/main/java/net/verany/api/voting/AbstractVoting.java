package net.verany.api.voting;

import org.bukkit.entity.Player;

import java.util.List;

public interface AbstractVoting<T> {

    void prepare(T... entries);

    void prepare(List<T> entryList);

    void vote(Player player, T value);

    void unVote(Player player);

    boolean hasVoted(Player player);

    boolean hasVotedForValue(Player player, T value);

    T getResult();

    int getVotes(T value);

    List<T> getValues();

}
