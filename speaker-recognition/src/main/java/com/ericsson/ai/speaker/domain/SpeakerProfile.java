package com.ericsson.ai.speaker.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Domain entity to link speaker name and profile ID.
 * 
 * @author W.Huang
 */
@Entity
@Table(name="SPEAKER_PROFILE")
public class SpeakerProfile
{
    @Id
    @Column(name = "UUID")
    private String _profileId;
    @Column(name = "name")
    private String _name;

    protected SpeakerProfile() {};

    public SpeakerProfile(String pProfileId, String pName)
    {
        _profileId = pProfileId;
        _name = pName;
    }

    public String getProfileId()
    {
        return _profileId;
    }

    public void setProfileId(String pProfileId)
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
