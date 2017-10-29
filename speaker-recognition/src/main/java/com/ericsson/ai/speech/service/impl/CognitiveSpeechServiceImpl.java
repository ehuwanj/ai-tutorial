package com.ericsson.ai.speech.service.impl;

import java.io.InputStream;
import java.util.UUID;

import com.ericsson.ai.speech.service.CognitiveSpeechService;

public class CognitiveSpeechServiceImpl implements CognitiveSpeechService
{

    @Override
    public void enrollSpeakerIdentification(InputStream pAudioStream, UUID pSpeakerId)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void identifySpeaker(InputStream pAudioStream)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void enrollSpeakerVerification(InputStream pAudioStream, UUID pSpeakerId)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void verifySpeaker(InputStream pAudioStream, UUID pSpeakerId)
    {
        // TODO Auto-generated method stub

    }

}
