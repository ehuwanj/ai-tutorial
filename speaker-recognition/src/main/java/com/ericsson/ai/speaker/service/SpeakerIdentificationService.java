package com.ericsson.ai.speaker.service;

import java.io.InputStream;
import java.util.UUID;

import com.microsoft.cognitive.speakerrecognition.contract.identification.EnrollmentOperation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.IdentificationOperation;

/**
 * Define the interface for Microsoft speaker identification service.
 *
 * @author W.Huang
 */
public interface SpeakerIdentificationService extends SpeakerService
{
    /**
     * Enroll a voice for speaker identification.
     *
     * @param pAudioStream
     * @param pSpeakerProfileId
     * @return EnrollmentOperation
     */
    EnrollmentOperation enrollIdentity(InputStream pAudioStream, UUID pSpeakerProfileId);

    /**
     * Reset all enrolled voices for specified speaker profile. 
     *
     * @param pSpeakerProfileId
     */
    void resetEnrollments(UUID pSpeakerProfileId);

    /**
     * Identify the speaker with provided voice and all profiles in this subscription.
     *
     * @param pAudioStream
     * @return IdentificationOperation
     */
    IdentificationOperation identifySpeaker(InputStream pAudioStream);
}
