package com.ericsson.ai.speaker.service.impl;

import java.util.UUID;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.ericsson.ai.speaker.domain.SpeakerProfile;
import com.ericsson.ai.speaker.service.SpeakerProfileDao;

@Repository
public class SpeakerProfileDaoImpl implements SpeakerProfileDao
{
    private EntityManager _entityManager;

    @Resource
    public void setEntityManager(EntityManager pEntityManager)
    {
        _entityManager = pEntityManager;
    }

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
