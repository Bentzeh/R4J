package no.stelar7.api.r4j.basic.constants.types.val;

import no.stelar7.api.r4j.basic.constants.types.CodedEnum;

import java.util.Optional;
import java.util.stream.Stream;

public enum TeamType implements CodedEnum<TeamType>
{
    RED("Red"),
    BLUE("Blue"),
    NEUTRAL("Neutral"),
    SOLO("THIS IS SOME BS VALUE"),
    ;
    
    
    private final String team;
    
    /**
     * Constructor for MapType
     *
     * @param code the mapId
     */
    TeamType(final String code)
    {
        this.team = code;
    }
    
    /**
     * Gets from code.
     *
     * @param mapId the map id
     * @return the from code
     */
    public Optional<TeamType> getFromCode(final String mapId)
    {
        if (mapId.length() > 15)
        {
            return Optional.of(SOLO);
        }
        
        return Stream.of(TeamType.values()).filter(t -> t.team.equals(mapId)).findFirst();
    }
    
    @Override
    public String prettyName()
    {
        switch (this)
        {
            default:
                return "This enum does not have a pretty name";
        }
    }
    
    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId()
    {
        return this.team;
    }
    
    
    /**
     * Used internaly in the api...
     *
     * @return the value
     */
    public String getValue()
    {
        return getId();
    }
    
}
