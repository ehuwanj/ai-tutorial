package com.ericsson.ai.speaker.service;

import com.ericsson.ai.speaker.domain.RequestProfile;
import com.ericsson.ai.speaker.domain.SpeakerProfile;

/**
 * Define common interface for speaker service.
 *
 * @author W.Huang
 */
public interface SpeakerService
{
    /**
     * Create a speaker profile for specified language and speaker name
     *
     * @param RequestProfile
     * @return SpeakerProfile
     */
    SpeakerProfile createSpeakerProfile(RequestProfile pRequestProfile);

    /**
     * Delete specified speaker profile.
     *
     * @param pProfileId
     */
    void deleteSpeakerProfile(String pProfileId);

    /**
     * Return the speaker profile with specified profile ID.
     *
     * @param pProfileId
     * @return SpeakerProfile
     */
    SpeakerProfile getSpeakerProfile(String pProfileId);
}
