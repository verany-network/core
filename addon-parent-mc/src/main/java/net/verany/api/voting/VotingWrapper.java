package net.verany.api.voting;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotingWrapper<T> implements AbstractVoting<T> {

    private final Map<T, Integer> votes = new HashMap<>();
    private final Map<Player, T> players = new HashMap<>();

    @SafeVarargs
    @Override
    public final void prepare(T... entries) {
        for (T entry : entries)
            votes.put(entry, 0);
    }

    @Override
    public void prepare(List<T> entryList) {
        entryList.forEach(this::prepare);
    }

    @Override
    public void vote(Player player, T value) {
        if (hasVoted(player))
            unVote(player);
        votes.put(value, votes.get(value) + 1);
        players.put(player, value);
    }

    @Override
    public void unVote(Player player) {
        votes.put(players.get(player), votes.get(players.get(player)) - 1);
        players.remove(player);
    }

    @Override
    public boolean hasVoted(Player player) {
        return players.containsKey(player);
    }

    @Override
    public boolean hasVotedForValue(Player player, T value) {
        return hasVoted(player) && players.get(player).equals(value);
    }

    @Override
    public T getResult() {
        int i = 0;
        T winner = null;

        for (T eachKey : votes.keySet())
            if (votes.get(eachKey) >= i) {
                i = votes.get(eachKey);
                winner = eachKey;
            }

        return winner;
    }

    @Override
    public int getVotes(T value) {
        return votes.get(value);
    }
}
