package com.navitas.integ.democustomerfacade.route;

import com.navitas.integ.common.exception.ValidationException;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Created by user on 22/06/2017.
 */
public class DemoCustomerFacadeRoute extends RouteBuilder {

    protected static final Logger LOG = LogManager.getLogger(DemoCustomerFacadeRoute.class);
    private String baseRouteId = "base-routeid-not-set!!!";
    private String frontEndReqEndpoint = "http-restful-endpoint-not-set!!!";
    private String backEndReqEndpoint = "aws-sqs://sqs-rqst-queue-not-set?accessKey=notset&amazonSQSClient=notset&waitTimeSeconds=20";
    private String backEndRespEndpoint = "sqs-resp-queue-not-set!!!";

    public DemoCustomerFacadeRoute() {
        super();
    }

    public DemoCustomerFacadeRoute(CamelContext context) {
        super(context);
    }

    @Override
    public void configure() throws Exception {

        final String logErrorUri = "log:" + this.getClass().getCanonicalName() + "?level=ERROR&showProperties=true&showException=true&showBody=true";
        final String logInfoUri = "log:" + this.getClass().getCanonicalName() + "?level=INFO&showProperties=true&showException=true&showBody=true";

        // RESTful Rqst/Reply: Return either:
        // Misc Error: httpstatus=500, "technical issue" but log more-detailed explanation
        // Bad Input: httpstatus=400, "bad request"+optional explanation
        onException(ValidationException.class)
                .handled(true)
                .useOriginalMessage()
                .to(logErrorUri)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .end();
        onException(Exception.class)
                .handled(true)
                .useOriginalMessage()
                .to(logErrorUri)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .end();

        rest("/{{demo-customer-facade.base-path}}")
                .
        from(frontEndReqEndpoint)
                .routeId(baseRouteId)
                .setExchangePattern(ExchangePattern.InOut)
                .to(logInfoUri + "&marker=FacadeRequestEntry")
                .inOut(backEndReqEndpoint)
                .to(logInfoUri + "&marker=FacadeResponseExit")
                .end();

    }

    public String getBaseRouteId() {
        return baseRouteId;
    }

    public void setBaseRouteId(String baseRouteId) {
        this.baseRouteId = baseRouteId;
    }

    public String getFrontEndReqEndpoint() {
        return frontEndReqEndpoint;
    }

    public void setFrontEndReqEndpoint(String frontEndReqEndpoint) {
        this.frontEndReqEndpoint = frontEndReqEndpoint;
    }

    public String getBackEndReqEndpoint() {
        return backEndReqEndpoint;
    }

    public void setBackEndReqEndpoint(String backEndReqEndpoint) {
        this.backEndReqEndpoint = backEndReqEndpoint;
    }

    public String getBackEndRespEndpoint() {
        return backEndRespEndpoint;
    }

    public void setBackEndRespEndpoint(String backEndRespEndpoint) {
        this.backEndRespEndpoint = backEndRespEndpoint;
    }
}
