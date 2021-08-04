package net.verany.api.voting;


import java.util.*;

public class VotingWrapper<T> implements AbstractVoting<T> {

    private final Map<T, Integer> votes = new HashMap<>();
    private final Map<UUID, T> players = new HashMap<>();

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
    public void vote(UUID uuid, T value) {
        if (hasVoted(uuid))
            unVote(uuid);
        votes.put(value, votes.get(value) + 1);
        players.put(uuid, value);
    }

    @Override
    public void unVote(UUID uuid) {
        votes.put(players.get(uuid), votes.get(players.get(uuid)) - 1);
        players.remove(uuid);
    }

    @Override
    public boolean hasVoted(UUID uuid) {
        return players.containsKey(uuid);
    }

    @Override
    public boolean hasVotedForValue(UUID uuid, T value) {
        return hasVoted(uuid) && players.get(uuid).equals(value);
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

    @Override
    public List<T> getValues() {
        return new ArrayList<>(votes.keySet());
    }
}
