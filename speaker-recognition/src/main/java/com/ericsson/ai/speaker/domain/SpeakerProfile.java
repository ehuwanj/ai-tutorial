package com.ericsson.ai.speaker.domain;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Domain entity to link speaker name and profile ID.
 * 
 * @author W.Huang
 */
@Entity
public class SpeakerProfile
{
    @Id
    private UUID _profileId;
    private String _name;

    public SpeakerProfile(UUID pProfileId, String pName)
    {
        _profileId = pProfileId;
        _name = pName;
    }

    public UUID getProfileId()
    {
        return _profileId;
    }

    public void setProfileId(UUID pProfileId)
    {
        _profileId = pProfileId;
    }

    public String getSpeakerName()
    {
        return _name;
    }

    public void setSpeakerName(String pName)
    {
        _name = pName;
    }
}
