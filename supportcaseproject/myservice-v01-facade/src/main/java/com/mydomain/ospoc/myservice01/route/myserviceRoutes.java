package com.mydomain.ospoc.myservice01.route;

import com.mydomain.ospoc.myservice01.model.Prospect;
//import com.mydomain.ospoc.common.exception.ValidationException;
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
public class myserviceRoutes extends RouteBuilder {

    protected static final Logger LOG = LogManager.getLogger(myserviceRoutes.class);

    public myserviceRoutes() {
        super();
    }

    public myserviceRoutes(CamelContext context) {
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
//        onException(ValidationException.class)
//                .handled(true)
//                .useOriginalMessage()
//                .to(logErrorUri)
//                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
//                .end();
        onException(Exception.class)
                .handled(true)
                .useOriginalMessage()
                .to(logErrorUri)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .end();

        // configure we want to use servlet as the component for the rest DSL
        // and we enable json binding mode
        restConfiguration().component("netty4-http").scheme("{{myservice-v01-facade.https_protocol}}").bindingMode(RestBindingMode.json)
                // and output using pretty print
                .dataFormatProperty("prettyPrint", "true")
                .host("localhost")
                .contextPath("/myservice01/v01").port(8080) // TODO: get from "{{shared-container-port}}"
        ;

        // this user REST service is json only
        rest("/prospects").id("enq-to-offer-vc-prospects-rest-service").description("Prospect REST service")
                    .consumes("application/json").produces("application/json")
                .get("/{id}").id("get-prospect-by-id").description("Find Prospect by id").outType(Prospect.class)
                    .param().name("id").type(RestParamType.path).description("The id of the user to get").dataType("int").endParam()
                    .to(logInfoUri+"&marker=getProspectById")
                    .to("{{myservice-v01-facade.myservice-v01-host-url-get-prospect}}")
//                .put().id("put-prospect-json-body").description("Updates or create a prospect").type(Prospect.class)
//                    .param().name("body").type(RestParamType.body).description("The prospect to update or create").endParam()
//                    .to(logInfoUri+"&marker=putProspect")
//                    .to("{{myservice-v01-facade.myservice-v01-host-url-put-prospect}}")
//                .get().id("get-prospects-all").description("Get all prospects").outTypeList(Prospect.class)
//                    .param().name("pagesize").type(RestParamType.query).defaultValue("10").allowableValues("10","20","50").endParam()
//                    .param().name("page").type(RestParamType.query).defaultValue("1").endParam()
//                    .to(logInfoUri+"&marker=getProspectPages")
//                    .to("{{myservice-v01-facade.myservice-v01-host-url-get-prospects}}")
        ;

    }
}
