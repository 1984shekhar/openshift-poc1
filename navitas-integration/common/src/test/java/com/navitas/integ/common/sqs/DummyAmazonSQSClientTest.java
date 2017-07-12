package com.navitas.integ.common.sqs;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.metrics.RequestMetricCollector;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.AddPermissionRequest;
import com.navitas.integ.common.sqs.DummyAmazonSQSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Used only for Unit Tests or if an AWS/SQS endpoint is being overridden.
 * It is required because just instantiating AmazonSQSClient verifies the credentials, which is not required
 * in a test environment.
 * Created by Lance Bryant on 04/07/2017.
 */
public class DummyAmazonSQSClientTest {

    private static final Logger LOG = LogManager.getLogger(DummyAmazonSQSClientTest.class);

    @Test
    public void T050_constructor_whenNoParameters_createsDefaultDummyClient() {
        AmazonSQSClient client = new DummyAmazonSQSClient();
        assertNotNull("Null DummyAmazonSQSClient returned!",client);
        assertEquals("client class wrong!",DummyAmazonSQSClient.class.getCanonicalName(),client.getClass().getCanonicalName());
        assertTrue("Not AWS Client!",client instanceof AmazonSQSClient);
    }


    @Test
    public void T051_constructor_whenClientConfigProvided_createsDefaultDummyClient() {
        ClientConfiguration config = new ClientConfiguration();
        AmazonSQSClient client = new DummyAmazonSQSClient(config);
        assertNotNull("Null DummyAmazonSQSClient returned!",client);
        assertEquals("client class wrong!",DummyAmazonSQSClient.class.getCanonicalName(),client.getClass().getCanonicalName());
        assertTrue("Not AWS Client!",client instanceof AmazonSQSClient);
    }

    @Test
    public void T052_constructor_whenCredentialsProvided_createsDefaultDummyClient() {
        AWSCredentials credentials = new BasicAWSCredentials("testAccessKey","testSecretKey");
        AmazonSQSClient client = new DummyAmazonSQSClient(credentials);
        assertNotNull("Null DummyAmazonSQSClient returned!",client);
        assertEquals("client class wrong!",DummyAmazonSQSClient.class.getCanonicalName(),client.getClass().getCanonicalName());
        assertTrue("Not AWS Client!",client instanceof AmazonSQSClient);
    }

    @Test
    public void T053_constructor_whenClientConfigAndCredentialsProvided_createsDefaultDummyClient() {
        AWSCredentials credentials = new BasicAWSCredentials("testAccessKey","testSecretKey");
        ClientConfiguration config = new ClientConfiguration();
        AmazonSQSClient client = new DummyAmazonSQSClient(credentials,config);
        assertNotNull("Null DummyAmazonSQSClient returned!",client);
        assertEquals("client class wrong!",DummyAmazonSQSClient.class.getCanonicalName(),client.getClass().getCanonicalName());
        assertTrue("Not AWS Client!",client instanceof AmazonSQSClient);
    }

}
