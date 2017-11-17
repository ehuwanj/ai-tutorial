package com.ericsson.ai.speaker.app;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.ericsson.ai.speaker.service.impl.SpeakerIdentificationServiceImpl;
import com.ericsson.ai.speaker.service.impl.SpeakerVerificationServiceImpl;
import com.microsoft.cognitive.speakerrecognition.SpeakerIdentificationClient;
import com.microsoft.cognitive.speakerrecognition.SpeakerIdentificationRestClient;
import com.microsoft.cognitive.speakerrecognition.SpeakerVerificationClient;
import com.microsoft.cognitive.speakerrecognition.SpeakerVerificationRestClient;
import com.microsoft.cognitive.speakerrecognition.contract.CreateProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.DeleteProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.EnrollmentException;
import com.microsoft.cognitive.speakerrecognition.contract.GetProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.identification.CreateProfileResponse;
import com.microsoft.cognitive.speakerrecognition.contract.identification.EnrollmentOperation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.IdentificationException;
import com.microsoft.cognitive.speakerrecognition.contract.identification.IdentificationOperation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.OperationLocation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.Profile;
import com.microsoft.cognitive.speakerrecognition.contract.verification.Enrollment;
import com.microsoft.cognitive.speakerrecognition.contract.verification.Verification;

public class SpeakerApplicationTest
{
	private static final String SUBSCRIPTION_KEY = "b32caa3d5f2046558b255a5c04bf198f";
	private static final String SPEAKER_IDENTIFICATION_PROFILE_UUID = "5ef9d03c-04ea-46ba-afff-ed98187ff775";
//	private static final String SPEAKER_VERIFICATION_PROFILE_UUID = SPEAKER_IDENTIFICATION_PROFILE_UUID; // verification requires its own profileID
	private static final String SPEAKER_VERIFICATION_PROFILE_UUID = "";
	private static final String LOCALE_EN_US = "en-us";
	private static final long WAIT_BEFORE_STATUS_CHECK = 3000;
    private static final boolean FORCE_SHORT_AUDIO = true;

	private SpeakerIdentificationClient _speakerIdentificationClient;
	private SpeakerIdentificationServiceImpl _speakerIdentificationService;

	private SpeakerVerificationClient _speakerVerificationClient;
	private SpeakerVerificationServiceImpl _speakerVerificationService;

	InputStream _identificationEnrollmentStream1 = null;
	InputStream _identificationEnrollmentStream2 = null;
	InputStream _identificationInputStream = null;

	InputStream _verificationPhrase1 = null;
	InputStream _verificationPhrase2 = null;
	InputStream _verificationPhrase3 = null;
//	InputStream _verificationInputStream = null;


	@Before
	public void setup()
	{
		_speakerIdentificationClient = new SpeakerIdentificationRestClient(SUBSCRIPTION_KEY);
		_speakerIdentificationService = new SpeakerIdentificationServiceImpl();
		_speakerIdentificationService.setSpeakerIdentificationClient(_speakerIdentificationClient);

		_speakerVerificationClient = new SpeakerVerificationRestClient(SUBSCRIPTION_KEY);
		_speakerVerificationService = new SpeakerVerificationServiceImpl();
		_speakerVerificationService.setSpeakerVerificationClient(_speakerVerificationClient);

		setupAudioStreams();
	}

	/**
	 * Setup audio streams
	 */
	private void setupAudioStreams()
	{
		try
		{
			_identificationEnrollmentStream1 = new FileInputStream("./src/test/resources/EnrollJuergen1.wav");
			_identificationEnrollmentStream2 = new FileInputStream("./src/test/resources/EnrollJuergen2.wav");
			_identificationInputStream = new FileInputStream("./src/test/resources/IdentifyJuergen1.wav");

			// TODO: create wav files for verification
			_verificationPhrase1 = new FileInputStream("./src/test/resources/VerificationPhrase1.wav");
			_verificationPhrase2 = new FileInputStream("./src/test/resources/VerificationPhrase2.wav");
			_verificationPhrase3 = new FileInputStream("./src/test/resources/VerificationPhrase3.wav");
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

    //
    // Test identification client
    //

	/**
	 * Test get all speaker profiles
	 *
	 * @throws IOException
	 * @throws GetProfileException
	 */
	@Test
	public void testSpeakerIdentificationClientGetProfiles() throws GetProfileException, IOException
	{
		List<Profile> speakerProfiles = _speakerIdentificationClient.getProfiles();

		speakerProfiles.forEach(p->System.out.println(
				"profile = " + p.identificationProfileId.toString() + "   status = " + p.enrollmentStatus
				));

		assertTrue(speakerProfiles.stream().anyMatch(p->p.identificationProfileId.toString().equals(SPEAKER_IDENTIFICATION_PROFILE_UUID)));
	}

	/**
	 * Test creating a speaker profile
	 * Deactivate this test, once the speaker profileID has been obtained
	 *
	 * @throws CreateProfileException
	 * @throws IOException
	 */
	//@Test
    public void testSpeakerIdentificationClientCreateProfile() throws CreateProfileException, IOException
    {
		CreateProfileResponse profileResponse = _speakerIdentificationClient.createProfile(LOCALE_EN_US);
		String speakerProfileUUID = profileResponse.identificationProfileId.toString();
		System.out.println(speakerProfileUUID);
    }

	/**
	 * Test delete speaker profile

	 * Deactivate this test, once the speaker profileID has been deleted
	 *
	 * @throws IOException
	 * @throws GetProfileException
	 * @throws DeleteProfileException
	 */
	//@Test
	public void testSpeakerIdentificationClientDeleteProfile() throws GetProfileException, IOException, DeleteProfileException
	{
		String deletableProfileId = "31277f3a-4e4f-44d4-8408-ef890aa4f868";
		_speakerIdentificationClient.deleteProfile(UUID.fromString(deletableProfileId));

		List<Profile> speakerProfiles = _speakerIdentificationClient.getProfiles();

		speakerProfiles.forEach(p->System.out.println(
				"profile = " + p.identificationProfileId.toString() + "   status = " + p.enrollmentStatus
				));

		assertTrue(speakerProfiles.stream().noneMatch(p->p.identificationProfileId.toString().equals(deletableProfileId)));
	}

    /**
     * Test enrollment of the speaker identification with long audio file
     *
     * @throws EnrollmentException
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testSpeakerIdentificationClientEnrollLongAudio() throws EnrollmentException, IOException, InterruptedException
    {
        OperationLocation operationLocation = _speakerIdentificationClient.enroll(_identificationEnrollmentStream1, UUID.fromString(SPEAKER_IDENTIFICATION_PROFILE_UUID));
        if(operationLocation != null)
        {
        	System.out.println(operationLocation.Url);

        	// wait some time to let enroll complete
        	Thread.sleep(WAIT_BEFORE_STATUS_CHECK);

        	EnrollmentOperation enrollmentOperation = _speakerIdentificationClient.checkEnrollmentStatus(operationLocation);

        	System.out.println(enrollmentOperation.processingResult.enrollmentSpeechTime);
        	System.out.println(enrollmentOperation.processingResult.remainingEnrollmentSpeechTime);
        	System.out.println(enrollmentOperation.processingResult.speechTime);
        	System.out.println(enrollmentOperation.processingResult.enrollmentStatus);
        }
    }

    /**
     * Test enrollment of the speaker identification with another short audio file
     *
     * @throws EnrollmentException
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testSpeakerIdentificationClientEnrollShortAudio() throws EnrollmentException, IOException, InterruptedException
    {
        OperationLocation operationLocation = _speakerIdentificationClient.enroll(_identificationEnrollmentStream2, UUID.fromString(SPEAKER_IDENTIFICATION_PROFILE_UUID), FORCE_SHORT_AUDIO);
        if(operationLocation != null)
        {
        	System.out.println(operationLocation.Url);

        	// wait some time to let enroll complete
        	Thread.sleep(WAIT_BEFORE_STATUS_CHECK);

        	EnrollmentOperation enrollmentOperation = _speakerIdentificationClient.checkEnrollmentStatus(operationLocation);

        	System.out.println(enrollmentOperation.processingResult.enrollmentSpeechTime);
        	System.out.println(enrollmentOperation.processingResult.remainingEnrollmentSpeechTime);
        	System.out.println(enrollmentOperation.processingResult.speechTime);
        	System.out.println(enrollmentOperation.processingResult.enrollmentStatus);
        }
    }

    /**
     * Test speaker identification with a short audio
     *
     * @throws IdentificationException
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testSpeakerIdentificationClientIdentify() throws IdentificationException, IOException, InterruptedException
    {
    	List<UUID> allProfileIds = Arrays.asList(UUID.fromString(SPEAKER_IDENTIFICATION_PROFILE_UUID));
    	OperationLocation operationLocation = _speakerIdentificationClient.identify(_identificationInputStream, allProfileIds, FORCE_SHORT_AUDIO);

        if(operationLocation != null)
        {
        	System.out.println(operationLocation.Url);

        	Thread.sleep(WAIT_BEFORE_STATUS_CHECK);
        	IdentificationOperation identificationOperation = _speakerIdentificationClient.checkIdentificationStatus(operationLocation);

        	System.out.println(identificationOperation.processingResult.identifiedProfileId.toString());
        	System.out.println(identificationOperation.processingResult.confidence);

        	// assert that identification was successful
        	assertEquals(SPEAKER_IDENTIFICATION_PROFILE_UUID, identificationOperation.processingResult.identifiedProfileId.toString());
        }
    }

    //
    // Test identification service
    //

    /**
     * Test speaker identification service for enrollment
     */
    @Test
    public void testSpeakerIdentificationServiceEnroll()
    {
    	EnrollmentOperation enrollmentOperation = _speakerIdentificationService.enrollSpeaker(_identificationEnrollmentStream1, UUID.fromString(SPEAKER_IDENTIFICATION_PROFILE_UUID));

    	System.out.println(enrollmentOperation.processingResult.enrollmentSpeechTime);
    	System.out.println(enrollmentOperation.processingResult.remainingEnrollmentSpeechTime);
    	System.out.println(enrollmentOperation.processingResult.speechTime);
    	System.out.println(enrollmentOperation.processingResult.enrollmentStatus);
    }

    /**
     * Test speaker identification service for identification (enrolled speaker profiles only)
     */
    @Test
    public void testSpeakerIdentificationServiceIdentify()
    {
    	IdentificationOperation identificationOperation = _speakerIdentificationService.identifySpeaker(_identificationInputStream);

    	System.out.println(identificationOperation.processingResult.identifiedProfileId.toString());
    	System.out.println(identificationOperation.processingResult.confidence);

    	// assert that identification was successful
    	assertEquals(SPEAKER_IDENTIFICATION_PROFILE_UUID, identificationOperation.processingResult.identifiedProfileId.toString());
    }


    //
    // Test verification client
    //
	/**
	 * Test creating a speaker profile
	 * Deactivate this test, once the speaker profileID has been obtained
	 *
	 * @throws CreateProfileException
	 * @throws IOException
	 */
	//@Test
    public void testSpeakerVerificationClientCreateProfile() throws CreateProfileException, IOException
    {
    	com.microsoft.cognitive.speakerrecognition.contract.verification.CreateProfileResponse profileResponse = _speakerVerificationClient.createProfile(LOCALE_EN_US);
		String speakerProfileUUID = profileResponse.verificationProfileId.toString();
		System.out.println(speakerProfileUUID);
    }

    //
    // Test verification service
    //

    /**
     *
     */
    @Test
    public void testSpeakerVerificationServiceEnroll()
    {
    	Enrollment enrollment = _speakerVerificationService.enrollPhrase(_verificationPhrase1, UUID.fromString(SPEAKER_VERIFICATION_PROFILE_UUID));

    	System.out.println(enrollment.enrollmentsCount);
    	System.out.println(enrollment.remainingEnrollments);
    	System.out.println(enrollment.phrase);
    	System.out.println(enrollment.enrollmentStatus);

    	enrollment = _speakerVerificationService.enrollPhrase(_verificationPhrase2, UUID.fromString(SPEAKER_VERIFICATION_PROFILE_UUID));

    	System.out.println(enrollment.enrollmentsCount);
    	System.out.println(enrollment.remainingEnrollments);
    	System.out.println(enrollment.phrase);
    	System.out.println(enrollment.enrollmentStatus);

    	enrollment = _speakerVerificationService.enrollPhrase(_verificationPhrase3, UUID.fromString(SPEAKER_VERIFICATION_PROFILE_UUID));

    	System.out.println(enrollment.enrollmentsCount);
    	System.out.println(enrollment.remainingEnrollments);
    	System.out.println(enrollment.phrase);
    	System.out.println(enrollment.enrollmentStatus);
    }

    @Test
    public void testSpeakerVerificationServiceIdentify()
    {
    	Verification verification = _speakerVerificationService.verifySpeaker(_verificationPhrase2);

    	System.out.println(verification.result);
    	System.out.println(verification.phrase);
    	System.out.println(verification.confidence);
    }

}
