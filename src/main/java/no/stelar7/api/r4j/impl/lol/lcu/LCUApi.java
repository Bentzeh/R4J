package no.stelar7.api.r4j.impl.lol.lcu;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import no.stelar7.api.r4j.basic.calling.DataCallBuilder;
import no.stelar7.api.r4j.basic.constants.api.*;
import no.stelar7.api.r4j.basic.constants.types.lol.*;
import no.stelar7.api.r4j.basic.exceptions.APIResponseException;
import no.stelar7.api.r4j.basic.utils.Pair;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Set;

public class LCUApi
{
    /**
     * Creates a lobby for the gamemode supplied
     * @param queue the queue to create
     * @return the partyid
     */
    public static String createLobby(GameQueueType queue)
    {
        Pair<String, String> header   = LCUConnection.getAuthorizationHeader();
        String               postData = "{\"queueId\":" + queue.getValues()[0] + "}";
        
        
        JsonObject obj = (JsonObject) new DataCallBuilder()
                .withLimiters(false)
                .withProxy(LCUConnection.getConnectionString() + Constants.GSVR)
                .withEndpoint(URLEndpoint.LCU_LOBBY)
                .withRequestMethod("POST")
                .withHeader(header.getKey(), header.getValue())
                .withPostData(postData)
                .build();
        
        String id = obj.get("partyId").getAsString();
        return id;
    }
    
    /**
     * Restarts the client after waitTimeout seconds
     * @param waitTimeout the time to wait in seconds
     */
    public static void restart(int waitTimeout)
    {
        Pair<String, String> header = LCUConnection.getAuthorizationHeader();
        
        new DataCallBuilder().withLimiters(false)
                             .withProxy(LCUConnection.getConnectionString() + Constants.GSVR)
                             .withEndpoint(URLEndpoint.LCU_RESTART)
                             .withHeader(header.getKey(), header.getValue())
                             .withQueryParameter("delaySeconds", String.valueOf(waitTimeout))
                             .withRequestMethod("POST")
                             .build();
    }
    
    /**
     * Invites the summoner to your game
     * @param name the name
     */
    public static void inviteSummoner(String name)
    {
        StringWriter sw = new StringWriter();
        try
        {
            JsonWriter jw  = new JsonWriter(sw);
            JsonObject obj = getSummoner(name);
            jw.beginArray()
              .beginObject()
              .name("toSummonerId").value(obj.get("summonerId").getAsString())
              .name("toSummonerName").value(obj.get("internalName").getAsString())
              .endObject()
              .endArray();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
        String               postData = sw.toString();
        Pair<String, String> header   = LCUConnection.getAuthorizationHeader();
        JsonArray obj = (JsonArray) new DataCallBuilder()
                .withLimiters(false)
                .withProxy(LCUConnection.getConnectionString() + Constants.GSVR)
                .withEndpoint(URLEndpoint.LCU_LOBBY_INVITE)
                .withRequestMethod("POST")
                .withHeader(header.getKey(), header.getValue())
                .withPostData(postData)
                .build();
    }
    
    /**
     * Starts the queue
     * <p>
     * @return true if able, false if unable to start
     *
     */
    public static boolean startMatchmaking()
    {
        Pair<String, String> header = LCUConnection.getAuthorizationHeader();
        try
        {
            new DataCallBuilder()
                    .withLimiters(false)
                    .withProxy(LCUConnection.getConnectionString() + Constants.GSVR)
                    .withEndpoint(URLEndpoint.LCU_LOBBY_MATCHMAKING)
                    .withRequestMethod("POST")
                    .withHeader(header.getKey(), header.getValue())
                    .build();
            
            return true;
        } catch (APIResponseException e)
        {
            return false;
        }
    }
    
    /**
     * Sets your roles in the lobby
     * @param primary first role
     * @param secondary second role
     */
    public static void setLobbyPositions(LCULobbyPositionType primary, LCULobbyPositionType secondary)
    {
        Pair<String, String> header = LCUConnection.getAuthorizationHeader();
        StringWriter         sw     = new StringWriter();
        try
        {
            JsonWriter jw = new JsonWriter(sw);
            jw.beginObject()
              .name("firstPreference").value(primary.name())
              .name("secondPreference").value(secondary.name())
              .endObject();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
        String postData = sw.toString();
        
        JsonObject obj = (JsonObject) new DataCallBuilder()
                .withLimiters(false)
                .withProxy(LCUConnection.getConnectionString() + Constants.GSVR)
                .withEndpoint(URLEndpoint.LCU_LOBBY_POSITION)
                .withRequestMethod("PUT")
                .withPostData(postData)
                .withHeader(header.getKey(), header.getValue())
                .build();
    }
    
    /**
     * Fetches summoner info
     * @param name the name to get info about
     * @return the info
     */
    public static JsonObject getSummoner(String name)
    {
        Pair<String, String> header = LCUConnection.getAuthorizationHeader();
        JsonArray arr = (JsonArray) new DataCallBuilder()
                .withLimiters(false)
                .withProxy(LCUConnection.getConnectionString() + Constants.GSVR)
                .withQueryParameter("name", name)
                .withEndpoint(URLEndpoint.LCU_SUMMONER_BY_NAME)
                .withRequestMethod("GET")
                .withHeader(header.getKey(), header.getValue())
                .build();
        
        JsonObject obj = arr.get(0).getAsJsonObject();
        
        return obj;
    }
    
    /**
     * Fetches summoner info
     * @param id the id to fetch info about
     * @return the info
     */
    public static JsonObject getSummoner(long id)
    {
        Pair<String, String> header = LCUConnection.getAuthorizationHeader();
        JsonObject arr = (JsonObject) new DataCallBuilder()
                .withLimiters(false)
                .withProxy(LCUConnection.getConnectionString() + Constants.GSVR)
                .withURLParameter(Constants.ID_PLACEHOLDER, String.valueOf(id))
                .withEndpoint(URLEndpoint.LCU_SUMMONER_BY_ID)
                .withRequestMethod("GET")
                .withHeader(header.getKey(), header.getValue())
                .build();
        
        return arr;
    }
    
    /**
     * Creates a notification
     *
     * @param title       title of notification
     * @param content     body of notification
     * @param icon        icon of notification
     * @param background  background image of notification
     * @param dismissable true if able to dismiss
     * @param state       read or unread
     * @return the notification id
     */
    public static int createNotification(String title, String content, String icon, String background, boolean dismissable, String state)
    {
        Pair<String, String> header = LCUConnection.getAuthorizationHeader();
        StringWriter         sw     = new StringWriter();
        try
        {
            JsonWriter jw = new JsonWriter(sw);
            jw.beginObject()
              .name("backgroundUrl").value(background)
              .name("created").value(LocalDateTime.now().toString())
              .name("dismissible").value(dismissable)
              .name("detailKey").value("pre_translated_details")
              .name("titleKey").value("pre_translated_title")
              .name("data").beginObject().name("title").value(title).name("content").value(content).endObject()
              .name("iconUrl").value(icon)
              .name("state").value(state)
              .endObject();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
        String postData = sw.toString();
        
        JsonObject obj = (JsonObject) new DataCallBuilder()
                .withLimiters(false)
                .withProxy(LCUConnection.getConnectionString() + Constants.GSVR)
                .withEndpoint(URLEndpoint.LCU_CREATE_NOTIFICATION)
                .withRequestMethod("POST")
                .withPostData(postData)
                .withHeader(header.getKey(), header.getValue())
                .build();
        
        return obj.get("id").getAsInt();
    }
    
    /**
     * Joins the lobby with the specified id
     * <p>
     * returns true if lobby exists, and we are able to join
     * @param id the lobbyid
     * @return true if able to join
     */
    public static boolean joinLobby(String id)
    {
        try
        {
            Pair<String, String> header = LCUConnection.getAuthorizationHeader();
            new DataCallBuilder()
                    .withLimiters(false)
                    .withProxy(LCUConnection.getConnectionString() + Constants.GSVR)
                    .withEndpoint(URLEndpoint.LCU_LOBBY_JOIN)
                    .withURLParameter(Constants.ID_PLACEHOLDER, id)
                    .withRequestMethod("POST")
                    .withHeader(header.getKey(), header.getValue())
                    .build();
            
            return true;
        } catch (APIResponseException e)
        {
            return false;
        }
    }
    
    /**
     * Attempts to login to the client
     *
     * @param username your username
     * @param password your password
     * @return the login object
     */
    public static JsonObject login(String username, String password)
    {
        Pair<String, String> header = LCUConnection.getAuthorizationHeader();
        StringWriter         sw     = new StringWriter();
        try
        {
            JsonWriter jw = new JsonWriter(sw);
            jw.beginObject()
              .name("username").value(username)
              .name("password").value(password)
              .endObject();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
        String postData = sw.toString();
        
        JsonObject obj = (JsonObject) new DataCallBuilder()
                .withLimiters(false)
                .withProxy(LCUConnection.getConnectionString() + Constants.GSVR)
                .withEndpoint(URLEndpoint.LCU_LOGIN)
                .withRequestMethod("POST")
                .withPostData(postData)
                .withHeader(header.getKey(), header.getValue())
                .build();
        
        return obj;
    }
    
    /**
     * Downloads replay
     * @param gameid the id to download
     */
    public static void downloadReplay(Long gameid)
    {
        Pair<String, String> header = LCUConnection.getAuthorizationHeader();
        StringWriter         sw     = new StringWriter();
        try
        {
            JsonWriter jw = new JsonWriter(sw);
            jw.beginObject()
              .name("componentType").value("string")
              .endObject();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
        String postData = sw.toString();
        
        JsonObject obj = (JsonObject) new DataCallBuilder()
                .withLimiters(false)
                .withProxy(LCUConnection.getConnectionString() + Constants.GSVR)
                .withEndpoint(URLEndpoint.LCU_REPLAY_DOWNLOAD)
                .withRequestMethod("POST")
                .withURLParameter(Constants.GAMEID_PLACEHOLDER, gameid.toString())
                .withPostData(postData)
                .withHeader(header.getKey(), header.getValue())
                .build();
    }
    
    /**
     * Makes a call to a url not specifically supported in the api
     *
     * @param url      the url to call
     * @param postData null if not a POST call
     * @param method the http method to use
     * @return whatever the api returns
     */
    public static Object customUrl(String url, String postData, String method)
    {
        Pair<String, String> header = LCUConnection.getAuthorizationHeader();
        
        DataCallBuilder obj = new DataCallBuilder()
                .withLimiters(false)
                .withProxy(LCUConnection.getConnectionString() + url)
                .withPostData(postData)
                .withRequestMethod(method)
                .withHeader(header.getKey(), header.getValue());
        
        
        return obj.build();
    }
    
    /**
     * Start spectator on game
     * @param gameid the id to spectate
     */
    public static void spectateGame(Long gameid)
    {
        Pair<String, String> header = LCUConnection.getAuthorizationHeader();
        StringWriter         sw     = new StringWriter();
        try
        {
            JsonWriter jw = new JsonWriter(sw);
            jw.beginObject()
              .name("componentType").value("string")
              .endObject();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
        String postData = sw.toString();
        
        JsonObject obj = (JsonObject) new DataCallBuilder()
                .withLimiters(false)
                .withProxy(LCUConnection.getConnectionString() + Constants.GSVR)
                .withEndpoint(URLEndpoint.LCU_REPLAY_WATCH)
                .withRequestMethod("POST")
                .withURLParameter(Constants.GAMEID_PLACEHOLDER, gameid.toString())
                .withPostData(postData)
                .withHeader(header.getKey(), header.getValue())
                .build();
    }
    
    /**
     * Fetches the replay save path
     * @return the path
     */
    public static String getReplaySavePath()
    {
        Pair<String, String> header = LCUConnection.getAuthorizationHeader();
        StringWriter         sw     = new StringWriter();
        String obj = (String) new DataCallBuilder()
                .withLimiters(false)
                .withProxy(LCUConnection.getConnectionString() + Constants.GSVR)
                .withEndpoint(URLEndpoint.LCU_REPLAY_DOWNLOAD_PATH)
                .withRequestMethod("GET")
                .withHeader(header.getKey(), header.getValue())
                .build();
        
        return obj;
    }
    
    public static Set<String> getWebsocketEvents()
    {
        String      obj2   = (String) LCUApi.customUrl("help", null, "GET");
        JsonElement parsed = JsonParser.parseString(obj2);
        JsonObject  events = parsed.getAsJsonObject().getAsJsonObject("events");
        return events.keySet();
    }
    
    
    /**
     * Creates a new websocket to the LCU
     * @return a websocket
     */
    public static LCUSocketReader createWebSocket()
    {
        return new LCUSocketReader();
    }
}
