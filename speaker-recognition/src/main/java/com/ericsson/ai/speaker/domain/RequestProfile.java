package com.ericsson.ai.speaker.domain;

public class RequestProfile
{
    private String _locale;
    private String _speakerName;

    /**
     * @return the locale
     */
    public String getLocale()
    {
        return _locale;
    }
    /**
     * @param pLocale the locale to set
     */
    public void setLocale(String pLocale)
    {
        _locale = pLocale;
    }
    /**
     * @return the speakerName
     */
    public String getSpeakerName()
    {
        return _speakerName;
    }
    /**
     * @param pSpeakerName the speakerName to set
     */
    public void setSpeakerName(String pSpeakerName)
    {
        _speakerName = pSpeakerName;
    }

}
