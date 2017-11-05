package com.ericsson.ai.speaker.dao.impl;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.ericsson.ai.speaker.dao.SpeakerProfileDao;
import com.ericsson.ai.speaker.domain.SpeakerProfile;

@Repository
public class SpeakerProfileDaoImpl implements SpeakerProfileDao
{
    @PersistenceContext
    private EntityManager _entityManager;

    @Override
    public SpeakerProfile getSpeakerProfile(UUID pProfileId)
    {
       return _entityManager.find(SpeakerProfile.class, pProfileId);
    }

    @Override
    @Transactional
    public void save(SpeakerProfile pSpeakerProfile)
    {
        _entityManager.persist(pSpeakerProfile);
    }

    @Override
    @Transactional
    public void delete(SpeakerProfile pSpeakerProfile)
    {
        _entityManager.remove(pSpeakerProfile);
    }

}
