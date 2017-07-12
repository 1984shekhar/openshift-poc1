package com.navitas.integ.common.sqs;

import com.amazonaws.services.sqs.AmazonSQSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Lance Bryant on 30/05/2017.
 */
public class AmazonSQSClientFactoryTest {

    private static final Logger LOG = LogManager.getLogger(AmazonSQSClientFactoryTest.class);

    @Test
    public void T050_constructor_whenInstantiated_thenFactoryItselfInstantiated() {
        AmazonSQSClientFactory factory = new AmazonSQSClientFactory();
        assertNotNull("factory was null!", factory);
        factory.setAccessKey("junitAccessKey1");
        factory.setSecretKey("junitSecretKey1");
        factory.setProxyUrl("http://myurl.com:123");
        factory.setProxyPort(80L);
    }


    @Test
    public void T051_constructor_whenProxyPortStringNumeric_thenFactoryItselfInstantiated() {
        AmazonSQSClientFactory factory = new AmazonSQSClientFactory();
        assertNotNull("factory was null!", factory);
        factory.setProxyPort("80");
        assertNotNull("factory proxyPort was null!", factory.getProxyPort());
        assertEquals("factory proxyPort wrong!", 80l, factory.getProxyPort().longValue());
    }

    @Test
    public void T052_constructor_whenProxyPortStringNonNumeric_thenFactoryItselfInstantiatedPortNull() {
        AmazonSQSClientFactory factory = new AmazonSQSClientFactory();
        assertNotNull("factory was null!", factory);
        factory.setProxyPort("NotNumeric");
        assertNull("factory proxyPort was not null!", factory.getProxyPort());
    }


    @Test
    public void T060_getAmazonSQSClient_whenFactoryInstantiatedAndAllPropsSet_thenReturnClient() {
        AmazonSQSClientFactory factory = new AmazonSQSClientFactory();
        assertNotNull("factory was null!", factory);
        factory.setAccessKey("junitAccessKey1");
        factory.setSecretKey("junitSecretKey1");
        factory.setProxyUrl("http://myurl.com:123");
        factory.setProxyPort(80L);
        factory.setUseRealSqsClient("false");
        AmazonSQSClient client = factory.getAmazonSQSClient();
        assertNotNull("Null AmazonSQSClient returned!", client);
        assertEquals("client class wrong!", DummyAmazonSQSClient.class.getCanonicalName(), client.getClass().getCanonicalName());
        assertTrue("Not AWS Client!", client instanceof AmazonSQSClient);

    }


}
