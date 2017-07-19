package com.navitas.integ.common.sqs;

//import com.amazonaws.ClientConfiguration;
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSCredentialsProvider;
//import com.amazonaws.metrics.RequestMetricCollector;
//import com.amazonaws.services.sqs.AmazonSQSClient;
//import com.amazonaws.services.sqs.model.AddPermissionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Used only for Unit Tests or if an AWS/SQS endpoint is being overridden.
 * It is required because just instantiating AmazonSQSClient verifies the credentials, which is not required
 * in a test environment.
 * Created by Lance Bryant on 04/07/2017.
 */
public class DummyAmazonSQSClient /* extends AmazonSQSClient */ {

    private static final Logger LOG = LoggerFactory.getLogger(DummyAmazonSQSClient.class);

    public DummyAmazonSQSClient() {
        LOG.debug("DummyAmazonSQSClient() constructor invoked.  No action taken.");
    }
//    public DummyAmazonSQSClient(ClientConfiguration clientConfiguration) {
//        LOG.debug("DummyAmazonSQSClient(ClientConfiguration clientConfiguration) constructor invoked.  No action taken.");
//    }
//    public DummyAmazonSQSClient(AWSCredentials awsCredentials) {
//        LOG.debug("DummyAmazonSQSClient(AWSCredentials awsCredentials) constructor invoked.  No action taken.");
//    }
//    public DummyAmazonSQSClient(AWSCredentials awsCredentials, ClientConfiguration clientConfiguration) {
//        LOG.debug("DummyAmazonSQSClient(AWSCredentials awsCredentials, ClientConfiguration clientConfiguration) constructor invoked.  No action taken.");
//    }
//    public DummyAmazonSQSClient(AWSCredentialsProvider awsCredentialsProvider) {
//        LOG.debug("DummyAmazonSQSClient(AWSCredentialsProvider awsCredentialsProvider) constructor invoked.  No action taken.");
//    }
//    public DummyAmazonSQSClient(AWSCredentialsProvider awsCredentialsProvider, ClientConfiguration clientConfiguration) {
//        LOG.debug("DummyAmazonSQSClient(AWSCredentialsProvider awsCredentialsProvider, ClientConfiguration clientConfiguration) constructor invoked.  No action taken.");
//    }
//    public DummyAmazonSQSClient(AWSCredentialsProvider awsCredentialsProvider, ClientConfiguration clientConfiguration, RequestMetricCollector requestMetricCollector) {
//        LOG.debug("DummyAmazonSQSClient() constructor invoked.  No action taken.");
//    }
//    public void addPermission(AddPermissionRequest addPermissionRequest) {
//        LOG.debug("addPermission(AddPermissionRequest addPermissionRequest) invoked.  No action taken.");
//    }
    public void addPermission(String queueUrl, String label, List<String> aWSAccountIds, List<String> actions) {
        LOG.debug("addPermission(String queueUrl, String label, List<String> aWSAccountIds, List<String> actions) invoked.  No action taken.");
    }
}
