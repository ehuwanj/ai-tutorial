package com.ericsson.ai.speaker.app;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.ai.speaker.service.SpeakerVerificationService;
import com.microsoft.cognitive.speakerrecognition.contract.verification.Enrollment;
import com.microsoft.cognitive.speakerrecognition.contract.verification.Verification;

@RestController
public class SpeakerVerificationRestControlller
{
    @Autowired
    private SpeakerVerificationService _verificationService;

    @PutMapping("/enroll/phrase/{profileId}")
    public Enrollment enrollPhrase(@PathVariable(name="profileId") String pSpeakerProfileId,
                                   @RequestBody InputStream pAudioStream)
    {
        return _verificationService.enrollPhrase(pAudioStream, UUID.fromString(pSpeakerProfileId));
    }

    @PutMapping("/verify")
    public Verification verify(@RequestBody InputStream pAudioStream)
    {
        return _verificationService.verifySpeaker(pAudioStream);
    }

}
