package com.navitas.integ.common.sqs;

//import com.amazonaws.ClientConfiguration;
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.sqs.AmazonSQSClient;
import com.navitas.integ.common.properties.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static com.navitas.integ.common.Constants.PROPERTY_SQS_USE_REAL;
import static com.navitas.integ.common.Constants.PROPERTY_VALUE_TRUE;

/**
 * Created by user on 30/05/2017.
 */
public abstract class AmazonSQSClientFactory {

    private static final Logger LOG = LoggerFactory.getLogger(AmazonSQSClientFactory.class);

    private String accessKey;
    private String secretKey;
    private String proxyUrl;
    private Long proxyPort;

    private static final Properties PROPERTIES = PropertiesUtils.loadProperties();

    public AmazonSQSClientFactory() {
        LOG.debug("abstract AmazonSQSClientFactory() constructor invoked");
    }

//    public AmazonSQSClient getAmazonSQSClient() {
//        LOG.debug("abstract AmazonSQSClientFactory.getAmazonSQSClient() starting...");
//        LOG.debug("abstract AmazonSQSClientFactory.getAmazonSQSClient() keys="+accessKey+"/"+secretKey+".");
//        Boolean doUseRealSqsClient = true;
//
//        if (PROPERTIES != null) {
//            LOG.debug("Properties loaded: "+PROPERTIES.toString());
//            String useRealSqsClientStr = PROPERTIES.getProperty(PROPERTY_SQS_USE_REAL);
//            if (PROPERTY_VALUE_TRUE.equals(useRealSqsClientStr)) {
//                doUseRealSqsClient = false;
//            }
//        }
//        AmazonSQSClient client;
//
//        if (doUseRealSqsClient) {
//            AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
//
//            if (proxyUrl != null && proxyPort != null) {
//                ClientConfiguration clientConfiguration = new ClientConfiguration();
//                clientConfiguration.setProxyHost(proxyUrl);
//                clientConfiguration.setProxyPort(proxyPort.intValue());
//                client = new AmazonSQSClient(awsCredentials, clientConfiguration);
//            } else {
//                client = new AmazonSQSClient(awsCredentials);
//            }
//
//        } else {
//            client = new DummyAmazonSQSClient();
//        }
//        return client;
//    }


    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public Long getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Long proxyPort) {
        this.proxyPort = proxyPort;
    }
}
