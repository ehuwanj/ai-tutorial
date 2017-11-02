package com.ericsson.ai.speaker.app;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.ai.speaker.domain.SpeakerProfile;

@RestController
public class SpeakerVerificationRestControlller
{
    @RequestMapping("/speakername")
    public SpeakerProfile getSpeakerName(@RequestParam(value="profileId") String pProfileId)
    {
        UUID profileId = UUID.randomUUID();
        return new SpeakerProfile(profileId, "ADMX");
    }
}
