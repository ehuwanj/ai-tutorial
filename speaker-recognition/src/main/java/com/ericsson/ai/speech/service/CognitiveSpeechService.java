package com.ericsson.ai.speech.service;

import java.io.InputStream;
import java.util.UUID;

public interface CognitiveSpeechService
{
    void enrollSpeakerIdentification(InputStream pAudioStream, UUID pSpeakerId);

    void identifySpeaker(InputStream pAudioStream);

    void enrollSpeakerVerification(InputStream pAudioStream, UUID pSpeakerId);

    void verifySpeaker(InputStream pAudioStream, UUID pSpeakerId);
}
