package com.mydomain.ospoc.enq2offer01.route;

import org.apache.camel.builder.RouteBuilder;
import com.mydomain.ospoc.enq2offer01.model.Prospect;
import org.apache.camel.CamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Created by user on 22/06/2017.
 */
public class TestRoutes extends RouteBuilder {

    protected static final Logger LOG = LogManager.getLogger(TestRoutes.class);

    public static final String API_ID_PROSPECTS_REST = "enq-to-offer-vc-prospects-rest-service";

    public static final String ROUTE_ID_GET_PROSPECT_BY_ID_REST = "get-prospect-by-id-main";
    public static final String ROUTE_ID_GET_PROSPECT_BY_ID_DIRECT = "get-prospect-by-id-direct";

    protected final String logErrorUri = "log:" + this.getClass().getCanonicalName() + "?level=ERROR&showProperties=true&showException=true&showBody=true";
    protected final String logInfoUri = "log:" + this.getClass().getCanonicalName() + "?level=INFO&showProperties=true&showException=true&showBody=true";
    protected final String logDebugUri = "log:" + this.getClass().getCanonicalName() + "?level=DEBUG&showProperties=true&showException=true&showBody=true";

    public TestRoutes() {
        super();
    }

    public TestRoutes(CamelContext context) {
        super(context);
    }

    @Override
    public void configure() throws Exception {

        LOG.info("TestRoutes.configure() starting...");

        // component "jetty", "http" and "http4" have also been tried...
        restConfiguration().component("netty4-http").scheme("http").bindingMode(RestBindingMode.auto)
                .dataFormatProperty("prettyPrint", "true")
                .host("localhost")
                .contextPath("/enq2offer01/v01").port(8080)
        ;

        LOG.info("TestRoutes.configure() about to create restDSL...");

// IF THE FOLLOWING "REST" DSL METHOD IS COMMENTED-OUT, THE ROUTE TEST OF THE "direct" ROUTE WORKS...
        rest("/prospects").id(API_ID_PROSPECTS_REST)
                //JSON only...
                    .consumes("application/json").produces("application/json")
                .get("/{id}") .id(ROUTE_ID_GET_PROSPECT_BY_ID_REST) .outType(Prospect.class)
                     .param().name("id").type(RestParamType.path).description("The id of the user to get").dataType("int").endParam()
                     .to(logInfoUri+"&marker=1_"+ROUTE_ID_GET_PROSPECT_BY_ID_REST)
                    .to("direct:"+ROUTE_ID_GET_PROSPECT_BY_ID_DIRECT)
        ;
// ...END OF PROBLEM REST DSL


        from("direct:"+ROUTE_ID_GET_PROSPECT_BY_ID_DIRECT)
                .routeId(ROUTE_ID_GET_PROSPECT_BY_ID_DIRECT)
                .to(logDebugUri+"&marker=1_"+ROUTE_ID_GET_PROSPECT_BY_ID_DIRECT)
                .to("{{enq2offer-v01-facade.enq2offer-v01-host-url-get-prospect}}")
                ;

        LOG.info("TestRoutes.configure() completed.");

    }
}
