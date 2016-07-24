package no.stelar7.api.l4j8.basic.constants;

import java.util.*;
import java.util.stream.*;

public enum AscencionType
{
    CHAMPION_ASCENDED,
    CLEAR_ASCENDED,
    MINION_ASCENDED;

    /**
     * Returns an AscentionType from the provided code
     *
     * @param code
     *            the lookup key
     * @return AscentionType
     */
    public static Optional<AscencionType> getFromCode(final String type)
    {
        return Stream.of(AscencionType.values()).filter(t -> t.name().equalsIgnoreCase(type)).findFirst();
    }

    /**
     * The code used to map strings to objects
     *
     * @return String
     */
    public String getCode()
    {
        return this.name();
    }
}
