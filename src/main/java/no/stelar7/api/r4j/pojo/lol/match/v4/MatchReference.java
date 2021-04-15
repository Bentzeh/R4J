package no.stelar7.api.r4j.pojo.lol.match.v4;

import no.stelar7.api.r4j.basic.constants.api.regions.LeagueShard;
import no.stelar7.api.r4j.basic.constants.types.lol.*;
import no.stelar7.api.r4j.impl.lol.builders.match.MatchBuilder;
import no.stelar7.api.r4j.impl.lol.builders.match.TimelineBuilder;
import no.stelar7.api.r4j.impl.lol.raw.DDragonAPI;
import no.stelar7.api.r4j.pojo.lol.staticdata.champion.StaticChampion;

import java.io.Serializable;
import java.time.*;

public class MatchReference implements Serializable
{
    private static final long serialVersionUID = 8945820538850500552L;
    
    private LaneType      lane;
    private long          gameId;
    private int         champion;
    private LeagueShard platformId;
    private long          timestamp;
    private GameQueueType queue;
    private RoleType      role;
    private SeasonType    season;
    
    
    public Match getFullMatch()
    {
        return new MatchBuilder().withPlatform(platformId).withId(gameId).get();
    }
    
    public MatchTimeline getTimeline()
    {
        return new TimelineBuilder().withPlatform(platformId).withId(gameId).get();
    }
    
    /**
     * championid associated with game.
     *
     * @return int
     */
    public int getChampionId()
    {
        return this.champion;
    }
    
    
    public StaticChampion getChampion()
    {
        return DDragonAPI.getInstance().getChampion(champion);
    }
    
    
    /**
     * LaneType associated with game
     *
     * @return String
     */
    public LaneType getLane()
    {
        return this.lane;
    }
    
    /**
     * Match ID.
     *
     * @return long
     */
    public long getGameId()
    {
        return this.gameId;
    }
    
    /**
     * Platform ID.
     *
     * @return String
     */
    public LeagueShard getPlatform()
    {
        return this.platformId;
    }
    
    /**
     * Queue.
     *
     * @return String
     */
    public GameQueueType getQueue()
    {
        return this.queue;
    }
    
    
    /**
     * RoleType
     *
     * @return String
     */
    public RoleType getRole()
    {
        return this.role;
    }
    
    /**
     * SeasonType
     *
     * @return String
     */
    public SeasonType getSeason()
    {
        return this.season;
    }
    
    /**
     * Timestamp
     *
     * @return long
     */
    public long getTimestamp()
    {
        return this.timestamp;
    }
    
    public ZonedDateTime getTimestampAsDate()
    {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.timestamp), ZoneOffset.UTC);
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        
        MatchReference that = (MatchReference) o;
        
        if (gameId != that.gameId)
        {
            return false;
        }
        if (timestamp != that.timestamp)
        {
            return false;
        }
        if (lane != that.lane)
        {
            return false;
        }
        if (champion != that.champion)
        {
            return false;
        }
        if (platformId != that.platformId)
        {
            return false;
        }
        if (queue != that.queue)
        {
            return false;
        }
        if (role != that.role)
        {
            return false;
        }
        return season == that.season;
    }
    
    @Override
    public int hashCode()
    {
        int result = lane != null ? lane.hashCode() : 0;
        result = 31 * result + (int) (gameId ^ (gameId >>> 32));
        result = 31 * result + champion;
        result = 31 * result + (platformId != null ? platformId.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (queue != null ? queue.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (season != null ? season.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString()
    {
        return "MatchReference{" +
               "champion=" + champion +
               ", gameId=" + gameId +
               ", timestamp=" + timestamp +
               ", lane='" + lane + '\'' +
               ", platformId='" + platformId + '\'' +
               ", queue='" + queue + '\'' +
               ", role='" + role + '\'' +
               ", season='" + season + '\'' +
               '}';
    }
}
