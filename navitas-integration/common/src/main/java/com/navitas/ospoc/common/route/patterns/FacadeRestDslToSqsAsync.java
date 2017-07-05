package com.navitas.ospoc.common.route.patterns;

import com.navitas.ospoc.common.exception.ValidationException;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

/**
 * Created by Lance Bryant on 04/07/2017.
 */
public class FacadeRestDslToSqsAsync extends RouteBuilder {

    protected final String logErrorUri = "log:" + this.getClass().getCanonicalName() + "?level=ERROR&showProperties=true&showException=true&showBody=true";
    protected final String logInfoUri = "log:" + this.getClass().getCanonicalName() + "?level=INFO&showProperties=true&showException=true&showBody=true";
    protected final String logDebugUri = "log:" + this.getClass().getCanonicalName() + "?level=DEBUG&showProperties=true&showException=true&showBody=true";

    public FacadeRestDslToSqsAsync() {
        super();
    }
    public FacadeRestDslToSqsAsync(CamelContext context) {
        super(context);
    }

    @Override
    public void configure() throws Exception {

//        // TODO: THe REST DSL has a better way to specify error response messages!!!
//        // RESTful Rqst/Reply: Return either:
//        // Bad Input: httpstatus=400, "bad request"+optional explanation
//        // Misc Error: httpstatus=500, "technical issue" but log more-detailed explanation
//        onException(ValidationException.class)
//                .handled(true)
//                .useOriginalMessage()
//                .to(logErrorUri+"&marker=OnValidationException")
//                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
//                .end();
//
//        onException(Exception.class)
//                .handled(true)
//                .useOriginalMessage()
//                .to(logErrorUri+"&marker=OnGeneralException")
//                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
//                .end();

    }
}
