package no.stelar7.api.r4j.impl.lor;

import no.stelar7.api.r4j.basic.calling.*;
import no.stelar7.api.r4j.basic.constants.api.*;
import no.stelar7.api.r4j.basic.constants.api.regions.RuneterraShard;
import no.stelar7.api.r4j.pojo.lor.ranked.*;

import java.util.*;

public class LORRankedAPI
{
    private static final LORRankedAPI INSTANCE = new LORRankedAPI();
    
    private LORRankedAPI()
    {
    }
    
    public static LORRankedAPI getInstance()
    {
        return LORRankedAPI.INSTANCE;
    }
    
    /**
     * Gets a list of all ranked players
     *
     * @param server region to get data from
     * @return LeagueList
     */
    public List<LoRPlayerRank> getLeaderboard(RuneterraShard server)
    {
        DataCallBuilder builder = new DataCallBuilder()
                .withHeader(Constants.X_RIOT_TOKEN_HEADER_KEY, DataCall.getCredentials().getLORAPIKey())
                .withEndpoint(URLEndpoint.V1_LOR_RANKED_LEADERBOARD)
                .withPlatform(server);
        
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("platform", server);
        
        Optional<?> chl = DataCall.getCacheProvider().get(URLEndpoint.V1_LOR_RANKED_LEADERBOARD, data);
        if (chl.isPresent())
        {
            return ((LoRRankedPlayerList) chl.get()).getPlayers();
        }
        
        try
        {
            LoRRankedPlayerList list = (LoRRankedPlayerList) builder.build();
            
            data.put("value", list);
            DataCall.getCacheProvider().store(URLEndpoint.V1_LOR_RANKED_LEADERBOARD, data);
            
            return list.getPlayers();
        } catch (ClassCastException e)
        {
            return null;
        }
    }
}
