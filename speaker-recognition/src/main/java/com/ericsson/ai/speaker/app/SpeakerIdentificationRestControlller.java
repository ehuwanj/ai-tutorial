package com.ericsson.ai.speaker.app;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.ai.speaker.domain.RequestProfile;
import com.ericsson.ai.speaker.domain.SpeakerProfile;
import com.ericsson.ai.speaker.service.SpeakerIdentificationService;
import com.microsoft.cognitive.speakerrecognition.contract.identification.EnrollmentOperation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.IdentificationOperation;

@RestController
public class SpeakerIdentificationRestControlller
{
    @Autowired
    private SpeakerIdentificationService _identificationService;

    @PostMapping("/speaker")
    public void createSpeakerProfile(RequestProfile pRequestProfile)
    {
        _identificationService.createSpeakerProfile(pRequestProfile);
    }

    @GetMapping("/speaker/{profileId}")
    public SpeakerProfile getSpeakerName(@PathVariable(name="profileId") String pProfileId)
    {
        return _identificationService.getSpeakerProfile(UUID.fromString(pProfileId));
    }

    @PutMapping("/enroll/speaker/{profileId}")
    public EnrollmentOperation enrollSpeaker(@PathVariable(name="profileId") String pSpeakerProfileId,
                                   @RequestBody InputStream pAudioStream)
    {
        return _identificationService.enrollSpeaker(pAudioStream, UUID.fromString(pSpeakerProfileId));
    }

    @PutMapping("/identify")
    public IdentificationOperation verify(@RequestBody InputStream pAudioStream)
    {
        return _identificationService.identifySpeaker(pAudioStream);
    }
}
 