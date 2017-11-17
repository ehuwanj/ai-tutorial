package com.ericsson.ai.speaker.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;
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

    @GetMapping("/speaker/voice")
    public InputStream getVoice()
    {
    	InputStream s = getDefaultIdentificationAudioStream();
        return s;
    }


    @GetMapping("/speaker/{profileId}")
    public SpeakerProfile getSpeakerName(@PathVariable(name="profileId") String pProfileId)
    {
        return _identificationService.getSpeakerProfile(pProfileId);
    }

    @PutMapping("/enroll/speaker/{profileId}")
    public EnrollmentOperation enrollSpeaker(@PathVariable(name="profileId") String pSpeakerProfileId,
                                   @RequestBody byte[] pAudioFile)
    {
    	return _identificationService.enrollSpeaker(getDefaultEnrollmentAudioStream(),
    			UUID.fromString(pSpeakerProfileId));
//    	return _identificationService.enrollSpeaker(
//    			pAudioFile == null ? getDefaultEnrollmentAudioStream() : getAudioStreamFromFile(pAudioFile.toString()),
//    			UUID.fromString(pSpeakerProfileId));
//        return _identificationService.enrollSpeaker(pAudioStream, UUID.fromString(pSpeakerProfileId));
    }

    @PutMapping("/identify")
    public IdentificationOperation verify(@RequestBody byte[] pAudioFile)
    {
    	return _identificationService.identifySpeaker(getDefaultIdentificationAudioStream());

//        return _identificationService.identifySpeaker(pAudioFile == null ? getDefaultIdentificationAudioStream() : getAudioStreamFromFile(pAudioFile.toString()));
//        return _identificationService.identifySpeaker(pAudioStream);
    }


	private InputStream getAudioStreamFromFile(String pAudioFile)
	{
		try
		{
			return new FileInputStream(pAudioFile);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private InputStream getDefaultIdentificationAudioStream()
	{
		try
		{
			return new FileInputStream("./src/test/resources/IdentifyJuergen1.wav");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private InputStream getDefaultEnrollmentAudioStream()
	{
		try
		{
			return new FileInputStream("./src/test/resources/EnrollJuergen1.wav");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
