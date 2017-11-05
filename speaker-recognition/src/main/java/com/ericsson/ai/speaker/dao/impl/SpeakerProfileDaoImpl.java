package com.ericsson.ai.speaker.dao.impl;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.ericsson.ai.speaker.dao.SpeakerProfileDao;
import com.ericsson.ai.speaker.domain.SpeakerProfile;

@Repository
public class SpeakerProfileDaoImpl implements SpeakerProfileDao
{
    private static Logger _logger = Logger.getLogger(SpeakerProfileDaoImpl.class.getName());

    @PersistenceContext
    private EntityManager _entityManager;

    @Override
    public SpeakerProfile getSpeakerProfile(String pProfileId)
    {
       SpeakerProfile speakerProfile = _entityManager.find(SpeakerProfile.class, pProfileId);
       if(speakerProfile == null)
       {
           _logger.warning("The speaker profile does not exist for ID: " + pProfileId);
       }
       return speakerProfile;
    }

    @Override
    @Transactional
    public void save(SpeakerProfile pSpeakerProfile)
    {
        if(pSpeakerProfile == null)
        {
            _logger.warning("The try to save a NULL speaker profile");
            return;
        }
        _entityManager.persist(pSpeakerProfile);
    }

    @Override
    @Transactional 
    public void delete(SpeakerProfile pSpeakerProfile)
    {
        if(pSpeakerProfile == null)
        {
            _logger.warning("The try to delete a NULL speaker profile");
            return;
        }
        _entityManager.remove(pSpeakerProfile);
    }

}
