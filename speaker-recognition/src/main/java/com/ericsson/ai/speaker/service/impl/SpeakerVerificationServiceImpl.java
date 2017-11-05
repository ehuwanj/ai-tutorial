package com.ericsson.ai.speaker.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

import com.ericsson.ai.speaker.dao.SpeakerProfileDao;
import com.ericsson.ai.speaker.domain.RequestProfile;
import com.ericsson.ai.speaker.domain.SpeakerProfile;
import com.ericsson.ai.speaker.service.SpeakerIdentificationService;
import com.ericsson.ai.speaker.service.SpeakerVerificationService;
import com.microsoft.cognitive.speakerrecognition.SpeakerVerificationClient;
import com.microsoft.cognitive.speakerrecognition.contract.CreateProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.DeleteProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.EnrollmentException;
import com.microsoft.cognitive.speakerrecognition.contract.ResetEnrollmentsException;
import com.microsoft.cognitive.speakerrecognition.contract.identification.IdentificationOperation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.Status;
import com.microsoft.cognitive.speakerrecognition.contract.verification.CreateProfileResponse;
import com.microsoft.cognitive.speakerrecognition.contract.verification.Enrollment;
import com.microsoft.cognitive.speakerrecognition.contract.verification.Verification;
import com.microsoft.cognitive.speakerrecognition.contract.verification.VerificationException;

/**
 * Implementation for {@link SpeakerVerificationService}
 *
 * @author W.Huang
 */
@Service("SpeakerVerificationService")
public class SpeakerVerificationServiceImpl implements SpeakerVerificationService
{
    private SpeakerProfileDao _speakerProfileDao;
    private SpeakerVerificationClient _speakerVerificationClient;
    private SpeakerIdentificationService _speakerIdentificationService;

    @Resource
    @Required
    public void setSpeakerProfileDao(SpeakerProfileDao pSpeakerProfileDao)
    {
        _speakerProfileDao = pSpeakerProfileDao;
    }

    @Resource
    @Required
    public void setSpeakerVerificationClient(SpeakerVerificationClient pSpeakerVerificationClient)
    {
        _speakerVerificationClient = pSpeakerVerificationClient;
    }

    @Resource
    @Required
    public void setSpeakerIdentificationService(SpeakerIdentificationService pSpeakerIdentificationService)
    {
        _speakerIdentificationService = pSpeakerIdentificationService;
    }

    @Override
    public SpeakerProfile createSpeakerProfile(RequestProfile pRequestProfile)
    {
        CreateProfileResponse profileResponse = null;
        try
        {
            profileResponse = _speakerVerificationClient.createProfile(pRequestProfile.getLocale());
        }
        catch (CreateProfileException | IOException e)
        {
            e.printStackTrace();
        }
        SpeakerProfile speakerProfile = null;
        if(profileResponse != null)
        {
             speakerProfile = new SpeakerProfile(profileResponse.verificationProfileId, pRequestProfile.getSpeakerName());
            _speakerProfileDao.save(speakerProfile);
        }
        return speakerProfile;
    }

    @Override
    public void deleteSpeakerProfile(UUID pProfileId)
    {
        try
        {
            _speakerVerificationClient.deleteProfile(pProfileId);
        }
        catch (DeleteProfileException | IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public SpeakerProfile getSpeakerProfile(UUID pProfileId)
    {
        return _speakerProfileDao.getSpeakerProfile(pProfileId);
    }

    @Override
    public Enrollment enrollPhrase(InputStream pAudioStream, UUID pSpeakerId)
    {
        try
        {
            return _speakerVerificationClient.enroll(pAudioStream, pSpeakerId);
        }
        catch (EnrollmentException | IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void resetEnrollments(UUID pSpeakerId)
    {
        try
        {
            _speakerVerificationClient.resetEnrollments(pSpeakerId);
        }
        catch (ResetEnrollmentsException | IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Verification verifySpeaker(InputStream pAudioStream)
    {
        IdentificationOperation operation = _speakerIdentificationService.identifySpeaker(pAudioStream);
        if(operation.status == Status.SUCCEEDED)
        {
            try
            {
                return _speakerVerificationClient.verify(pAudioStream, operation.processingResult.identifiedProfileId);
            }
            catch (VerificationException | IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.err.println("Can not identify the speaker with provided audio stream.");
        }
        return null;
    }

}
