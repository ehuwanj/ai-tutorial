package com.ericsson.ai.speaker.service;

import java.io.InputStream;
import java.util.UUID;

import com.microsoft.cognitive.speakerrecognition.contract.verification.Enrollment;
import com.microsoft.cognitive.speakerrecognition.contract.verification.Verification;

/**
 * Define the interface for Microsoft speaker verification service.
 *
 * @author W.Huang
 */
public interface SpeakerVerificationService extends SpeakerService
{

    /**
     * Enroll the speaker phrase for verification.
     *
     * @param pAudioStream
     * @param pSpeakerId
     * @return Enrollment
     */
    Enrollment enrollPhrase(InputStream pAudioStream, UUID pSpeakerId);

    /**
     * Reset all verification phrase for specified speaker.
     * 
     * @param pSpeakerId
     */
    void resetEnrollments(UUID pSpeakerId);

    /**
     * Identify and verify the speaker with provided voice.
     * 
     * @param pAudioStream
     * @param pSpeakerId
     * @return Verification
     */
    Verification verifySpeaker(InputStream pAudioStream);
}
