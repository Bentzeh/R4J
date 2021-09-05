package no.stelar7.api.r4j.impl.lol.raw;

import no.stelar7.api.r4j.basic.calling.*;
import no.stelar7.api.r4j.basic.constants.api.*;
import no.stelar7.api.r4j.basic.constants.api.regions.*;
import no.stelar7.api.r4j.basic.utils.Utils;
import no.stelar7.api.r4j.pojo.lol.match.v5.LOLMatch;
import no.stelar7.api.r4j.pojo.lol.tournament.*;

import java.util.List;

@SuppressWarnings("unchecked")
public final class TournamentAPI
{
    
    private static final TournamentAPI INSTANCE      = new TournamentAPI(false);
    private static final TournamentAPI INSTANCE_STUB = new TournamentAPI(true);
    
    private boolean useStub;
    
    public void useStub(boolean useStub)
    {
        this.useStub = useStub;
    }
    
    public static TournamentAPI getInstance(boolean useStub)
    {
        return useStub ? INSTANCE_STUB : INSTANCE;
    }
    
    private TournamentAPI(boolean useStub)
    {
        this.useStub = useStub;
    }
    
    
    public boolean isStub()
    {
        return useStub;
    }
    
    /**
     * Generates a list of tournamentCodes to use for adding players to games.
     * You should only use a code per match.
     *
     * @param count        the amount of codes to generate (default: 1)
     * @param params       the TournamentCodeParameters for the games played with this code
     * @param tournamentId the tournamentId this game is played on
     * @return a list of tournamentcodes
     */
    public List<String> generateTournamentCodes(final TournamentCodeParameters params, final long tournamentId, Integer count)
    {
        DataCallBuilder builder = new DataCallBuilder().withHeader(Constants.X_RIOT_TOKEN_HEADER_KEY, DataCall.getCredentials().getTournamentAPIKey())
                                                       .withQueryParameter(Constants.URL_PARAM_TOURNAMENT_COUNT, String.valueOf(count != null ? count : 1))
                                                       .withQueryParameter(Constants.URL_PARAM_TOURNAMENT_ID, String.valueOf(tournamentId))
                                                       .withEndpoint(URLEndpoint.V4_TOURNAMENT_CODES)
                                                       .withPostData(Utils.getGson().toJson(params))
                                                       .withRequestMethod(Constants.METHOD_POST)
                                                       .withPlatform(RegionShard.AMERICAS);
        
        if (useStub)
        {
            builder.withEndpoint(URLEndpoint.V4_TOURNAMENT_STUB_CODES);
        }
        
        try
        {
            return (List<String>) builder.build();
        } catch (ClassCastException e)
        {
            
            return null;
        }
    }
    
    /**
     * A list of all games played with this tournament code.
     *
     * @param tournamentCode The tournament code of the match
     * @param platform the platform
     * @return a list of matchIds played with this code
     */
    public List<Long> getMatchIds(final LeagueShard platform, final String tournamentCode)
    {
        DataCallBuilder builder = new DataCallBuilder().withHeader(Constants.X_RIOT_TOKEN_HEADER_KEY, DataCall.getCredentials().getTournamentAPIKey())
                                                       .withURLParameter(Constants.TOURNAMENT_CODE_PLACEHOLDER, tournamentCode)
                                                       .withEndpoint(URLEndpoint.V4_TOURNAMENT_MATCHLIST)
                                                       .withPlatform(platform);
        
        try
        {
            return (List<Long>) builder.build();
        } catch (ClassCastException e)
        {
            
            return null;
        }
    }
    
    /**
     * Get details about a match from this tournament.
     * Differs from Match.getMatchInfo because this returns participants
     * Platform is the platform the game was played on
     *
     * @param server         the server the games are played on
     * @param tournamentCode The tournament code of the match
     * @param matchId        the ID of the match.
     * @return Match
     */
    public LOLMatch getMatchInfo(final LeagueShard server, final String tournamentCode, final Long matchId)
    {
        DataCallBuilder builder = new DataCallBuilder().withHeader(Constants.X_RIOT_TOKEN_HEADER_KEY, DataCall.getCredentials().getTournamentAPIKey())
                                                       .withURLParameter(Constants.TOURNAMENT_CODE_PLACEHOLDER, tournamentCode)
                                                       .withURLParameter(Constants.MATCH_ID_PLACEHOLDER, String.valueOf(matchId))
                                                       .withEndpoint(URLEndpoint.V4_TOURNAMENT_MATCH)
                                                       .withPlatform(server);
        try
        {
            return (LOLMatch) builder.build();
        } catch (ClassCastException e)
        {
            
            return null;
        }
    }
    
    /**
     * Get info about a specified tournament
     *
     * @param tournamentCode The tournament code of the match
     * @return TournamentCode
     */
    public TournamentCode getTournamentInfo(final String tournamentCode)
    {
        DataCallBuilder builder = new DataCallBuilder().withHeader(Constants.X_RIOT_TOKEN_HEADER_KEY, DataCall.getCredentials().getTournamentAPIKey())
                                                       .withQueryParameter(Constants.TOURNAMENT_CODE_PLACEHOLDER, tournamentCode)
                                                       .withEndpoint(URLEndpoint.V4_TOURNAMENT_CODES_BY_CODE)
                                                       .withPlatform(RegionShard.AMERICAS);
        
        if (useStub)
        {
            throw new IllegalArgumentException("This method is not useable with the stub API");
        }
        
        try
        {
            return (TournamentCode) builder.build();
        } catch (ClassCastException e)
        {
            
            return null;
        }
    }
    
    /**
     * Get info about what happened in the lobby.
     *
     * @param tournamentCode The tournament code of the match
     * @return Lobby events
     */
    public List<LobbyEvent> getTournamentLobbyInfo(final String tournamentCode)
    {
        DataCallBuilder builder = new DataCallBuilder().withHeader(Constants.X_RIOT_TOKEN_HEADER_KEY, DataCall.getCredentials().getTournamentAPIKey())
                                                       .withURLParameter(Constants.TOURNAMENT_CODE_PLACEHOLDER, tournamentCode)
                                                       .withEndpoint(URLEndpoint.V4_TOURNAMENT_LOBBY_EVENTS)
                                                       .withPlatform(RegionShard.AMERICAS);
        
        if (useStub)
        {
            builder.withEndpoint(URLEndpoint.V4_TOURNAMENT_STUB_LOBBY_EVENTS);
        }
        
        try
        {
            LobbyEventWrapper lew = (LobbyEventWrapper) builder.build();
            return lew.getEventList();
        } catch (ClassCastException e)
        {
            
            return null;
        }
    }
    
    /**
     * Providers will need to call this endpoint first.
     * Registers their callback URL and their API key with the tournament system.
     * before this, other tournament endpoints will not work.
     * The id will always be the same for a given URL and Server.
     * I.E. Passing http://website.com and EUW will ALWAYS return the same code.
     *
     * @param params the provider definition
     * @return provider id
     */
    public Long registerAsProvider(final ProviderRegistrationParameters params)
    {
        DataCallBuilder builder = new DataCallBuilder().withHeader(Constants.X_RIOT_TOKEN_HEADER_KEY, DataCall.getCredentials().getTournamentAPIKey())
                                                       .withEndpoint(URLEndpoint.V4_TOURNAMENT_PROVIDER)
                                                       .withPostData(params.toJson())
                                                       .withRequestMethod(Constants.METHOD_POST)
                                                       .withPlatform(RegionShard.AMERICAS);
        
        
        if (useStub)
        {
            builder.withEndpoint(URLEndpoint.V4_TOURNAMENT_STUB_PROVIDER);
        }
        
        try
        {
            return Long.parseLong(builder.build().toString());
        } catch (NumberFormatException e)
        {
            return null;
        }
    }
    
    /**
     * Returns a TournamentId used to refer to this tournament later on.
     *
     * @param params the tournament definition
     * @return the id
     */
    public Long registerTournament(final TournamentRegistrationParameters params)
    {
        DataCallBuilder builder = new DataCallBuilder().withHeader(Constants.X_RIOT_TOKEN_HEADER_KEY, DataCall.getCredentials().getTournamentAPIKey())
                                                       .withEndpoint(URLEndpoint.V4_TOURNAMENT_TOURNAMENT)
                                                       .withPostData(Utils.getGson().toJson(params))
                                                       .withRequestMethod(Constants.METHOD_POST)
                                                       .withPlatform(RegionShard.AMERICAS);
        
        if (useStub)
        {
            builder.withEndpoint(URLEndpoint.V4_TOURNAMENT_STUB_TOURNAMENT);
        }
        
        try
        {
            return Long.parseLong(builder.build().toString());
        } catch (NumberFormatException e)
        {
            return null;
        }
    }
    
    /**
     * Update the data for the tournamet code
     *
     * @param params         the tournament definition
     * @param tournamentCode The tournament code of the match
     */
    public void updateTournament(final String tournamentCode, final TournamentCodeUpdateParameters params)
    {
        DataCallBuilder builder = new DataCallBuilder().withHeader(Constants.X_RIOT_TOKEN_HEADER_KEY, DataCall.getCredentials().getTournamentAPIKey())
                                                       .withQueryParameter(Constants.TOURNAMENT_CODE_PLACEHOLDER, tournamentCode)
                                                       .withEndpoint(URLEndpoint.V4_TOURNAMENT_CODES)
                                                       .withPostData(Utils.getGson().toJson(params))
                                                       .withRequestMethod(Constants.METHOD_PUT)
                                                       .withPlatform(RegionShard.AMERICAS);
        
        if (useStub)
        {
            throw new IllegalArgumentException("This method is not useable with the stub API");
        }
        
        builder.build();
    }
}
