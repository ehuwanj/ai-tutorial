package com.ericsson.ai.speaker.dao;

import com.ericsson.ai.speaker.domain.SpeakerProfile;

public interface SpeakerProfileDao
{
    SpeakerProfile getSpeakerProfile(String pProfileId);

    void save(SpeakerProfile pSpeakerProfile);

    void delete(SpeakerProfile pSpeakerProfile);
}
