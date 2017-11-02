package com.ericsson.ai.speaker.service;

import java.util.UUID;

import com.ericsson.ai.speaker.domain.SpeakerProfile;

public interface SpeakerProfileDao
{
    SpeakerProfile getSpeakerProfile(UUID pProfileId);

    void save(SpeakerProfile pSpeakerProfile);

    void delete(SpeakerProfile pSpeakerProfile);
}
