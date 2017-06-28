package com.navitas.ospoc.enq2offer01.route;

import com.navitas.ospoc.enq2offer01.model.Prospect;
import com.navitas.ospoc.common.exception.ValidationException;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Created by user on 22/06/2017.
 */
public class Enq2OfferRoutes extends RouteBuilder {

    protected static final Logger LOG = LogManager.getLogger(Enq2OfferRoutes.class);
//    private String baseRouteId = "base-routeid-not-set!!!";
//    private String frontEndReqEndpoint = "http-restful-endpoint-not-set!!!";
//    private String backEndReqEndpoint = "aws-sqs://sqs-rqst-queue-not-set?accessKey=notset&amazonSQSClient=notset&waitTimeSeconds=20";
//    private String backEndRespEndpoint = "sqs-resp-queue-not-set!!!";

    public Enq2OfferRoutes() {
        super();
    }

    public Enq2OfferRoutes(CamelContext context) {
        super(context);
    }

    @Override
    public void configure() throws Exception {

        final String logErrorUri = "log:" + this.getClass().getCanonicalName() + "?level=ERROR&showProperties=true&showException=true&showBody=true";
        final String logInfoUri = "log:" + this.getClass().getCanonicalName() + "?level=INFO&showProperties=true&showException=true&showBody=true";

        // TODO: THe REST DSL has a better way to specify error response messages!!!
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

        // configure we want to use servlet as the component for the rest DSL
        // and we enable json binding mode
        restConfiguration().component("netty4-http").scheme("{{enq2offer-v01-facade.https_protocol}}").bindingMode(RestBindingMode.json)
                // and output using pretty print
                .dataFormatProperty("prettyPrint", "true")
                .host("localhost")
                // setup context path and port number that netty will use
                .contextPath("/enq2offer01/v01").port(8080) // TODO: get from "{{shared-container-port}}"
//                // add swagger api-doc out of the box (TODO: remove? It isn't going to work anyway in camel/rest)
//                .apiContextPath("/api-doc")
//                .apiProperty("api.title", "User API").apiProperty("api.version", "1.2.3")
//                // and enable CORS (TODO: Not yet!!!)
//                .apiProperty("cors", "true")
        ;

        // this user REST service is json only
        rest("/prospects").id("enq-to-offer-vc-prospects-rest-service").description("Prospect REST service")
                    .consumes("application/json").produces("application/json")
                .get("/{id}").id("get-prospect-by-id").description("Find Prospect by id").outType(Prospect.class)
                    .param().name("id").type(RestParamType.path).description("The id of the user to get").dataType("int").endParam()
                    .to(logInfoUri+"&marker=getProspectById")
                    .to("{{enq2offer-v01-facade.enq2offer-v01-host-url-get-prospect}}")
                .put().id("put-prospect-json-body").description("Updates or create a prospect").type(Prospect.class)
                    .param().name("body").type(RestParamType.body).description("The prospect to update or create").endParam()
                    .to(logInfoUri+"&marker=putProspect")
                    .to("{{enq2offer-v01-facade.enq2offer-v01-host-url-put-prospect}}")
                .get().id("get-prospects-all").description("Get all prospects").outTypeList(Prospect.class)
                    .param().name("pagesize").type(RestParamType.query).defaultValue("10").allowableValues("10","20","50").endParam()
                    .param().name("page").type(RestParamType.query).defaultValue("1").endParam()
                    .to(logInfoUri+"&marker=getProspectPages")
                    .to("{{enq2offer-v01-facade.enq2offer-v01-host-url-get-prospects}}")
        ;

    }
}
