package no.stelar7.api.r4j.pojo.lol.match.v5;

import java.io.Serializable;

public class TimelinePosition implements Serializable
{
    private static final long serialVersionUID = 7605395081863390628L;
    
    private int x;
    private int y;
    
    
    /**
     * The X position
     *
     * @return int
     */
    public int getX()
    {
        return this.x;
    }
    
    /**
     * The Y position
     *
     * @return int
     */
    public int getY()
    {
        return this.y;
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
        
        TimelinePosition that = (TimelinePosition) o;
        
        if (x != that.x)
        {
            return false;
        }
        return y == that.y;
    }
    
    @Override
    public int hashCode()
    {
        int result = x;
        result = 31 * result + y;
        return result;
    }
    
    @Override
    public String toString()
    {
        return "MatchPosition{" +
               "x=" + x +
               ", y=" + y +
               '}';
    }
}
