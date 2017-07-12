package com.navitas.integ.common.sqs;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.navitas.integ.common.Constants.PROPERTY_VALUE_FALSE;

/**
 * Created by Lance Bryant on 30/05/2017.
 */
public class AmazonSQSClientFactory {

    private static final Logger LOG = LogManager.getLogger(AmazonSQSClientFactory.class);

    private String accessKey;
    private String secretKey;
    private String proxyUrl;
    private Long proxyPort;
    private String useRealSqsClient;

    public AmazonSQSClientFactory() {
        LOG.debug("abstract AmazonSQSClientFactory() constructor invoked");
    }

    /**
     * Returns an AmazonSQSClient, either a real one or, if PROPERTY_SQS_USE_REAL is false, a dummy one for unit-testing.
     * (The real AmazonSQSClient verifies the credentials with Amazon on instantiation hence the need for a dummy one for
     * unit testing)
     *
     * @return
     */
    public AmazonSQSClient getAmazonSQSClient() {
        LOG.debug("abstract AmazonSQSClientFactory.getAmazonSQSClient() starting...");
        LOG.debug("abstract AmazonSQSClientFactory.getAmazonSQSClient() keys=" + accessKey + "/" + secretKey + ".");
        Boolean doUseRealSqsClient = true;

        if (PROPERTY_VALUE_FALSE.equals(useRealSqsClient)) {
            doUseRealSqsClient = false;
        }
        AmazonSQSClient client;

        if (doUseRealSqsClient) {
            AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

            if (proxyUrl != null && proxyPort != null) {
                ClientConfiguration clientConfiguration = new ClientConfiguration();
                clientConfiguration.setProxyHost(proxyUrl);
                clientConfiguration.setProxyPort(proxyPort.intValue());
                client = new AmazonSQSClient(awsCredentials, clientConfiguration);
            } else {
                client = new AmazonSQSClient(awsCredentials);
            }

        } else {
            LOG.debug("getAmazonSQSClient: Return DummyAmazonSQSClient for unit-testing");
            client = new DummyAmazonSQSClient();
        }
        return client;
    }


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
        LOG.debug("setProxyPort(Long proxyPort) invoked");
        this.proxyPort = proxyPort;
    }

    public void setProxyPort(String proxyPortString) {
        LOG.debug("setProxyPort(String proxyPortString) invoked");
        try {
            Long proxyPort = new Long(proxyPortString);
            this.proxyPort = proxyPort;
        } catch (Exception e) {
            LOG.error("setProxyPort failed: " + e.toString() + " >>>> null assumed!!!");
        }

    }

    public String getUseRealSqsClient() {
        return useRealSqsClient;
    }

    public void setUseRealSqsClient(String useRealSqsClient) {
        this.useRealSqsClient = useRealSqsClient;
    }
}
