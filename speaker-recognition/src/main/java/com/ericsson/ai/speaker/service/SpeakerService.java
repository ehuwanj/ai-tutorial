package com.ericsson.ai.speaker.service;

import java.util.UUID;

import com.ericsson.ai.speaker.domain.SpeakerProfile;

/**
 * Define common interface for speaker service.
 *
 * @author W.Huang
 */
public interface SpeakerService
{
    /**
     * Create a speaker profile for specified language
     *
     * @param pLocale
     * @param pSpeakerName
     * @return UserProfile
     */
    SpeakerProfile createSpeakerProfile(String pLocale, String pSpeakerName);

    /**
     * Delete specified speaker profile.
     *
     * @param pProfileId
     */
    void deleteSpeakerProfile(UUID pProfileId);

    /**
     * Return the speaker name with specified profile ID.
     *
     * @param pProfileId
     * @return speaker name
     */
    String getSpeakerName(UUID pProfileId);
}
