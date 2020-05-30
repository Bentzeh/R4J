package no.stelar7.api.r4j.basic.constants.api.regions;

import no.stelar7.api.r4j.basic.constants.types.RealmSpesificEnum;

import java.util.Optional;
import java.util.stream.Stream;

public enum RegionalName implements RealmSpesificEnum
{
    /**
     * Unknown platform, used for bots in participant identities
     */
    UNKNOWN("", ""),
    /**
     * Americas platform.
     */
    AMERICAS("AMERICAS", "americas"),
    /**
     * Europe platform.
     */
    EUROPE("EUROPE", "europe"),
    /**
     * Asia platform.
     */
    ASIA("ASIA", "asia"),
    /**
     * GARENA platform.
     */
    GARENA("GARENA", "garena"),
    /**
     * PBE platform.
     */
    PBE("PBE", "pbe");
    
    private String[] key;
    
    RegionalName(String... s)
    {
        this.key = s;
    }
    
    /**
     * Returns a Platform from the provided code
     *
     * @param code the lookup key
     * @return Platform from code
     */
    public static Optional<RegionalName> getFromCode(final String code)
    {
        return Stream.of(RegionalName.values()).filter(t -> t.key[0].equalsIgnoreCase(code)).findFirst();
    }
    
    
    /**
     * Used internaly in the api...
     *
     * @return the value
     */
    public String getValue()
    {
        return this.key[0];
    }
    
    /**
     * Used internaly in the api...
     *
     * @return the value
     */
    @Override
    public String getRealmValue()
    {
        return this.key[1];
    }
    
    @Override
    public String toString()
    {
        return this.getValue();
    }
}
