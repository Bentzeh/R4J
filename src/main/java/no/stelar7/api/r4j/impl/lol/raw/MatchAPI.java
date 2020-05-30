package no.stelar7.api.r4j.impl.lol.raw;

import no.stelar7.api.r4j.basic.constants.api.regions.LeagueShard;
import no.stelar7.api.r4j.basic.utils.LazyList;
import no.stelar7.api.r4j.impl.lol.builders.match.*;
import no.stelar7.api.r4j.pojo.lol.match.*;

import java.util.List;

public final class MatchAPI
{
    private static final MatchAPI INSTANCE = new MatchAPI();
    
    public static MatchAPI getInstance()
    {
        return MatchAPI.INSTANCE;
    }
    
    private MatchAPI()
    {
        // Hide public constructor
    }
    
    public List<MatchReference> getMatchList(LeagueShard server, String accountId, Integer beginIndex, Integer endIndex)
    {
        return new MatchListBuilder(server, accountId, beginIndex, endIndex).get();
    }
    
    /**
     * Returns a list of all games avaliable in the api
     * This list is updated lazily!
     * To get all the items, either iterate the list once, or get(Integer.MAX_VALUE)
     *
     * @param server    the platform the account is on
     * @param accountId the account to check
     * @return {@code List<MatchReference>}
     */
    public LazyList<MatchReference> getMatchList(LeagueShard server, String accountId)
    {
        int increment = 100;
        return new LazyList<>(increment, prevValue -> getMatchList(server, accountId, prevValue, prevValue + increment));
    }
    
    /**
     * Returns an iterator that transforms all {@code MatchReference} to {@code Match}
     *
     * @param server    the platform the account is on
     * @param accountId the account to check
     * @return {@code MatchIterator}
     */
    public MatchIterator getMatchIterator(LeagueShard server, String accountId)
    {
        return new MatchIterator(getMatchList(server, accountId));
    }
    
    /**
     * Returns the match data from a match id
     *
     * @param server  the platform the match was played on
     * @param matchId the id to check
     * @return Match
     */
    public Match getMatch(LeagueShard server, long matchId)
    {
        return new MatchBuilder(server, matchId).get();
    }
    
    /**
     * Returns the timeline relating to a match.
     * Not avaliable for matches older than a year
     *
     * @param server  the platform to find the match on
     * @param matchId the matchId to find timeline for
     * @return MatchTimeline if avaliable
     */
    public MatchTimeline getTimeline(LeagueShard server, long matchId)
    {
        return new TimelineBuilder(server, matchId).get();
    }
}
