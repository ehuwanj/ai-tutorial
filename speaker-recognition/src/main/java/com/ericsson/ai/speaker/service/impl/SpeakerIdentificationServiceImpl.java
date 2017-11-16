package com.ericsson.ai.speaker.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

import com.ericsson.ai.speaker.dao.SpeakerProfileDao;
import com.ericsson.ai.speaker.domain.RequestProfile;
import com.ericsson.ai.speaker.domain.SpeakerProfile;
import com.ericsson.ai.speaker.service.SpeakerIdentificationService;
import com.microsoft.cognitive.speakerrecognition.SpeakerIdentificationClient;
import com.microsoft.cognitive.speakerrecognition.contract.CreateProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.DeleteProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.EnrollmentException;
import com.microsoft.cognitive.speakerrecognition.contract.EnrollmentStatus;
import com.microsoft.cognitive.speakerrecognition.contract.GetProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.ResetEnrollmentsException;
import com.microsoft.cognitive.speakerrecognition.contract.identification.CreateProfileResponse;
import com.microsoft.cognitive.speakerrecognition.contract.identification.EnrollmentOperation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.IdentificationException;
import com.microsoft.cognitive.speakerrecognition.contract.identification.IdentificationOperation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.OperationLocation;

/**
 * Implementation for {@link SpeakerIdentificationService}
 *
 * @author W.Huang
 */
@Service("SpeakerIdentificationService")
public class SpeakerIdentificationServiceImpl implements SpeakerIdentificationService
{
    private static Logger _logger = Logger.getLogger(SpeakerIdentificationServiceImpl.class.getName());

    /**
     * waiting time to let enroll or identify complete before doing the status check
     */
    private final static long WAIT_BEFORE_STATUS_CHECK = 3000;

    /**
     * force short audio
     */
    private final static boolean FORCE_SHORT_AUDIO = true;

    private SpeakerProfileDao _speakerProfileDao;
    private SpeakerIdentificationClient _speakerIdentificationClient;
    private List<UUID> allProfileIds;

    @Resource
    @Required
    public void setSpeakerProfileDao(SpeakerProfileDao pSpeakerProfileDao)
    {
        _speakerProfileDao = pSpeakerProfileDao;
    }

    @Resource
    @Required
    public void setSpeakerIdentificationClient(SpeakerIdentificationClient pSpeakerIdentificationClient)
    {
        _speakerIdentificationClient = pSpeakerIdentificationClient;
    }

    @Override
    public SpeakerProfile createSpeakerProfile(RequestProfile pRequestProfile)
    {
        CreateProfileResponse profileResponse = null;
        try
        {
            profileResponse = _speakerIdentificationClient.createProfile(pRequestProfile.getLocale());
        }
        catch (CreateProfileException | IOException e)
        {
            e.printStackTrace();
        }
        UUID profileId = (profileResponse != null) ? profileResponse.identificationProfileId : UUID.randomUUID();
        _logger.info(profileId.toString());
        SpeakerProfile speakerProfile = new SpeakerProfile(profileId.toString(), pRequestProfile.getSpeakerName());
        _speakerProfileDao.save(speakerProfile);
        return speakerProfile;
    }

    @Override
    public void deleteSpeakerProfile(String pProfileId)
    {
        try
        {
            _speakerIdentificationClient.deleteProfile(UUID.fromString(pProfileId));
        } catch (DeleteProfileException | IOException e)
        {
            e.printStackTrace();
            return;
        }
        SpeakerProfile speakerProfile = _speakerProfileDao.getSpeakerProfile(pProfileId);
        _speakerProfileDao.delete(speakerProfile);
    }

    @Override
    public SpeakerProfile getSpeakerProfile(String pProfileId)
    {
        return _speakerProfileDao.getSpeakerProfile(pProfileId);
    }

    @Override
    public EnrollmentOperation enrollSpeaker(InputStream pAudioStream, UUID pSpeakerId)
    {
        OperationLocation operationLocation = null;
        try
        {
            operationLocation = _speakerIdentificationClient.enroll(pAudioStream, pSpeakerId, FORCE_SHORT_AUDIO);
        }
        catch (EnrollmentException | IOException e)
        {
            e.printStackTrace();
        }
        if(operationLocation != null)
        {
            try
            {
            	Thread.sleep(WAIT_BEFORE_STATUS_CHECK);
                return _speakerIdentificationClient.checkEnrollmentStatus(operationLocation);
            }
            catch (EnrollmentException | IOException | InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void resetEnrollments(UUID pSpeakerProfileId)
    {
        try
        {
            _speakerIdentificationClient.resetEnrollments(pSpeakerProfileId);
        }
        catch (ResetEnrollmentsException | IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public IdentificationOperation identifySpeaker(InputStream pAudioStream)
    {
        if(allProfileIds == null)
        {
            allProfileIds = getAllProfiles();
        }
        OperationLocation operationLocation;
        try
        {
            operationLocation = _speakerIdentificationClient.identify(pAudioStream, allProfileIds, FORCE_SHORT_AUDIO);
        }
        catch (IdentificationException | IOException e)
        {
            e.printStackTrace();
            return null;
        }
        if(operationLocation != null)
        {
            try
            {
            	Thread.sleep(WAIT_BEFORE_STATUS_CHECK);
                return _speakerIdentificationClient.checkIdentificationStatus(operationLocation);
            }
            catch (IdentificationException | IOException | InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    private List<UUID> getAllProfiles()
    {
        try
        {
            return _speakerIdentificationClient.getProfiles()
                                                        .stream()
                                                        .filter(p -> p.enrollmentStatus == EnrollmentStatus.ENROLLED)
                                                        .map(p -> p.identificationProfileId)
                                                        .collect(Collectors.toList());
        }
        catch (GetProfileException | IOException e)
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
