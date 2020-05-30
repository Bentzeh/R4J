package no.stelar7.api.r4j.pojo.lol.tournament;

import no.stelar7.api.r4j.basic.constants.api.regions.LeagueShard;

import java.io.Serializable;

public class ProviderRegistrationParameters implements Serializable
{
    private static final long serialVersionUID = 7896252954178900155L;
    
    private LeagueShard region;
    private String      url;
    
    public ProviderRegistrationParameters(final LeagueShard region, final String callbackUrl)
    {
        this.region = region;
        this.url = callbackUrl;
    }
    
    public String toJson()
    {
        return String.format("{\"region\":\"%s\",\"url\":\"%s\"}", region.getRealmValue().toUpperCase(), url);
    }
    
    /**
     * The provider's callback URL to which tournament game results in this region should be posted.
     * The URL must be well-formed, use the http or https protocol, and use the default port for the protocol (http URLs must use port 80, https URLs must use port 443).
     *
     * @return callbackUrl
     */
    public String getCallbackUrl()
    {
        return this.url;
    }
    
    public void setCallbackUrl(final String url)
    {
        this.url = url;
    }
    
    /**
     * The region in which the provider will be running tournaments.
     *
     * @return platform
     */
    public LeagueShard getRegion()
    {
        return this.region;
    }
    
    public void setRegion(final LeagueShard region)
    {
        this.region = region;
    }
    
    
    @Override
    public int hashCode()
    {
        final int prime  = 31;
        int       result = 1;
        result = (prime * result) + ((this.region == null) ? 0 : this.region.hashCode());
        result = (prime * result) + ((this.url == null) ? 0 : this.url.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (this.getClass() != obj.getClass())
        {
            return false;
        }
        final ProviderRegistrationParameters other = (ProviderRegistrationParameters) obj;
        if (this.region != other.region)
        {
            return false;
        }
        if (this.url == null)
        {
            return other.url == null;
        } else
        {
            return this.url.equals(other.url);
        }
    }
    
    @Override
    public String toString()
    {
        return "ProviderRegistrationParameters [region=" + this.region + ", url=" + this.url + "]";
    }
    
}
