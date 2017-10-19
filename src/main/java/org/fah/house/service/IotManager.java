package org.fah.house.service;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import org.fah.house.domain.Climate;
import org.fah.house.domain.iot.ClimateSensor;
import org.fah.house.util.PrivateKeyReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;

@Service
public class IotManager {

	private static String IOT_CLIENT_ENDPOINT = "a1m396isagwlan.iot.us-west-2.amazonaws.com"; 
	private static String IOT_CLIENT_ID = "803137715327"; 

	@Autowired
	private ApplicationContext context;
	
	private AWSIotMqttClient aClient = null;

	
	public void publishClimateSensor(String roomName, Climate climate) throws AWSIotException, InterruptedException
	{
		String thingName = generateClimateThingName(roomName);
		ClimateSensor lSensor = new ClimateSensor(thingName, Math.round(climate.getTemperature()), 0, Math.round(climate.getHumidity()));
		AWSIotMqttClient lClient = createIotClient();
		long reportInterval = 300000;            // milliseconds. Default 
		lSensor.setReportInterval(reportInterval);
		lClient.attach(lSensor);
		lClient.connect();
        // Delete existing document if any
        lSensor.delete();
        lSensor.activate();
		// lClient.publish(TestTopic, "boo");
	}
	
	private String generateClimateThingName(String roomName) 
	{
		return StringUtils.replace(roomName, " ", "") + "Climate";
	}

	private AWSIotMqttClient createIotClient()
	{
		if (aClient == null)
		{
			KeyStorePasswordPair pair;
			try {
				pair = getKeyStorePasswordPair();
				aClient = new AWSIotMqttClient(IOT_CLIENT_ENDPOINT, IOT_CLIENT_ID, pair.keyStore, pair.keyPassword);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return aClient;
	}
	
	private KeyStorePasswordPair getKeyStorePasswordPair() throws IOException {
        return getKeyStorePasswordPair(null);
    }

   private KeyStorePasswordPair getKeyStorePasswordPair(String keyAlgorithm) throws IOException {
	   
	    String publicKeyFileLocation = System.getProperty("iot.public.key.resource");
	    publicKeyFileLocation = StringUtils.isEmpty(publicKeyFileLocation) ? "classpath:dedb0e773f-certificate.pem.crt" : publicKeyFileLocation;

	    String privateKeyFileLocation = System.getProperty("iot.private.key.resource");
	    privateKeyFileLocation = StringUtils.isEmpty(privateKeyFileLocation) ? "classpath:dedb0e773f-private.pem.key" : privateKeyFileLocation;

		Resource publicKeyFile = context.getResource(publicKeyFileLocation);
		Resource privateKeyFile = context.getResource(privateKeyFileLocation);	
		
        if (!publicKeyFile.exists() || !privateKeyFile.exists()) {
            System.out.println("Certificate or private key file missing");
            return null;
        }

        Certificate certificate = loadCertificateFromFile(publicKeyFile);
        PrivateKey privateKey = loadPrivateKeyFromFile(privateKeyFile, keyAlgorithm);
        if (certificate == null || privateKey == null) {
            return null;
        }

        return getKeyStorePasswordPair(certificate, privateKey);
    }

    private KeyStorePasswordPair getKeyStorePasswordPair(Certificate certificate, PrivateKey privateKey) {
        KeyStore keyStore = null;
        String keyPassword = null;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            keyStore.setCertificateEntry("alias", certificate);

            // randomly generated key password for the key in the KeyStore
            keyPassword = new BigInteger(128, new SecureRandom()).toString(32);
            keyStore.setKeyEntry("alias", privateKey, keyPassword.toCharArray(), new Certificate[] { certificate });
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            System.out.println("Failed to create key store");
            return null;
        }

        return new KeyStorePasswordPair(keyStore, keyPassword);
    }
	
	 private  Certificate loadCertificateFromFile(Resource fileResource) throws IOException {
	        Certificate certificate = null;

	        File file = fileResource.getFile();
	        if (!file.exists()) {
	            System.out.println("Certificate file not found: " + fileResource);
	            return null;
	        }
	        
	        try (BufferedInputStream stream = new BufferedInputStream(fileResource.getInputStream())) {
	            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
	            certificate = certFactory.generateCertificate(stream);
	        } catch (IOException | CertificateException e) {
	            System.out.println("Failed to load certificate file " + fileResource);
	        }

	        return certificate;
	    }

	    private  PrivateKey loadPrivateKeyFromFile(Resource fileResource, String algorithm) throws IOException {
	        PrivateKey privateKey = null;

	        File file = fileResource.getFile();
	        if (!file.exists()) {
	            System.out.println("Private key file not found: " + fileResource);
	            return null;
	        }
	        try (DataInputStream stream = new DataInputStream(fileResource.getInputStream())) {
	            privateKey = PrivateKeyReader.getPrivateKey(stream, algorithm);
	        } catch (IOException | GeneralSecurityException e) {
	            System.out.println("Failed to load private key from file " + fileResource);
	        }

	        return privateKey;
	    }

	    public static class KeyStorePasswordPair {
	        public KeyStore keyStore;
	        public String keyPassword;

	        public KeyStorePasswordPair(KeyStore keyStore, String keyPassword) {
	            this.keyStore = keyStore;
	            this.keyPassword = keyPassword;
	        }
	    }
}
