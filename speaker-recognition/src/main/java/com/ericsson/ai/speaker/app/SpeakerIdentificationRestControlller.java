package com.ericsson.ai.speaker.app;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.ai.speaker.service.SpeakerIdentificationService;

@RestController
public class SpeakerIdentificationRestControlller
{
    private SpeakerIdentificationService _identificationService;

    @Resource
    @Required
    public void setSpeakerIdentificationService(SpeakerIdentificationService pSpeakerIdentificationService)
    {
        _identificationService = pSpeakerIdentificationService;
    }

    @RequestMapping("/speakername")
    public String getSpeakerName(@RequestParam(value="profileId") String pProfileId)
    {
        return _identificationService.getSpeakerName(UUID.fromString(pProfileId));
    };
}
