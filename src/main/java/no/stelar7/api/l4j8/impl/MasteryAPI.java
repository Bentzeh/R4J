package no.stelar7.api.l4j8.impl;

import com.sun.istack.internal.Nullable;
import no.stelar7.api.l4j8.basic.calling.*;
import no.stelar7.api.l4j8.basic.constants.api.*;
import no.stelar7.api.l4j8.pojo.championmastery.ChampionMastery;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public final class MasteryAPI
{
    
    private static final MasteryAPI INSTANCE = new MasteryAPI();
    
    public static MasteryAPI getInstance()
    {
        return MasteryAPI.INSTANCE;
    }
    
    private MasteryAPI()
    {
        // Hide public constructor
    }
    
    
    /**
     * The response object contains the summoners masteryscore.
     *
     * @param server     the region to execute against
     * @param summonerId the summonerId
     * @return Optional FeaturedGames
     */
    public Integer getMasteryScore(Platform server, long summonerId)
    {
        DataCallBuilder builder = new DataCallBuilder().withURLParameter(Constants.SUMMONER_ID_PLACEHOLDER, String.valueOf(summonerId))
                                                       .withEndpoint(URLEndpoint.V3_MASTERY_SCORE)
                                                       .withPlatform(server);
        
        
        Optional chl = DataCall.getCacheProvider().get(URLEndpoint.V3_MASTERY_SCORE, server, summonerId);
        if (chl.isPresent())
        {
            return (Integer) chl.get();
        }
        
        Integer list = (Integer) builder.build();
        DataCall.getCacheProvider().store(URLEndpoint.V3_MASTERY_SCORE, list, server, summonerId);
        return list;
    }
    
    /**
     * Gets the champions with the most masteryscore from the summoner
     *
     * @param server     the server the summoner plays on
     * @param summonerId the summonerid of the summoner
     * @param count      the amount of champions to get
     * @return A sorted list of ChampionMastery
     */
    public List<ChampionMastery> getTopChampions(Platform server, long summonerId, @Nullable Integer count)
    {
        List<ChampionMastery> list = getChampionMasteries(server, summonerId);
        
        return list.stream().sorted(Comparator.comparing(ChampionMastery::getChampionPoints))
                   .limit(count != null ? count : 3)
                   .collect(Collectors.toList());
        
    }
    
    
    /**
     * The response object contains the summoners mastery of a champion.
     * Only championid and summonerid is present if the level == 0
     *
     * @param server     the region to execute against
     * @param summonerId the summonerId
     * @param championId the championId
     * @return Optional ChampionMastery
     */
    public ChampionMastery getChampionMastery(Platform server, long summonerId, int championId)
    {
        DataCallBuilder builder = new DataCallBuilder().withURLParameter(Constants.SUMMONER_ID_PLACEHOLDER, String.valueOf(summonerId))
                                                       .withURLParameter(Constants.CHAMPION_ID_PLACEHOLDER, String.valueOf(championId))
                                                       .withEndpoint(URLEndpoint.V3_MASTERY_BY_CHAMPION)
                                                       .withPlatform(server);
        
        
        Optional chl = DataCall.getCacheProvider().get(URLEndpoint.V3_MASTERY_BY_CHAMPION, server, summonerId, championId);
        if (chl.isPresent())
        {
            return (ChampionMastery) chl.get();
        }
        
        ChampionMastery mastery = (ChampionMastery) builder.build();
        
        if (mastery == null)
        {
            try
            {
                mastery = new ChampionMastery();
                
                Field player = mastery.getClass().getDeclaredField("playerId");
                player.setAccessible(true);
                player.set(mastery, summonerId);
                
                Field champ = mastery.getClass().getDeclaredField("championId");
                champ.setAccessible(true);
                champ.set(mastery, championId);
                
                Field level = mastery.getClass().getDeclaredField("championLevel");
                level.setAccessible(true);
                level.set(mastery, 0);
            } catch (NoSuchFieldException | IllegalAccessException e)
            {
                Logger.getGlobal().warning("Class has changed, please fix me");
            }
        }
        
        DataCall.getCacheProvider().store(URLEndpoint.V3_MASTERY_BY_CHAMPION, mastery, server, summonerId, championId);
        return mastery;
    }
    
    
    /**
     * The response object contains a list of the summoners mastery of champions.
     * Does not return a value for champions with mastery level 0
     *
     * @param server     the region to execute against
     * @param summonerId the summonerId
     * @return Optional ChampionMastery
     */
    public List<ChampionMastery> getChampionMasteries(Platform server, long summonerId)
    {
        DataCallBuilder builder = new DataCallBuilder().withURLParameter(Constants.SUMMONER_ID_PLACEHOLDER, String.valueOf(summonerId))
                                                       .withEndpoint(URLEndpoint.V3_MASTERY_BY_ID)
                                                       .withPlatform(server);
        
        
        Optional chl = DataCall.getCacheProvider().get(URLEndpoint.V3_MASTERY_BY_ID, server, summonerId);
        if (chl.isPresent())
        {
            return (List<ChampionMastery>) chl.get();
        }
        
        List<ChampionMastery> list = (List<ChampionMastery>) builder.build();
        DataCall.getCacheProvider().store(URLEndpoint.V3_MASTERY_BY_ID, list, server, summonerId);
        return list;
    }
    
}
