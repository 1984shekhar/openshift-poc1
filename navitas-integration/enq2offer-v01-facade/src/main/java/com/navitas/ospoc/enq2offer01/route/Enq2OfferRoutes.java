package com.navitas.ospoc.enq2offer01.route;

import com.navitas.ospoc.common.route.patterns.FacadeRestDslToSqsAsync;
import com.navitas.ospoc.enq2offer01.model.Prospect;
import org.apache.camel.CamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Created by user on 22/06/2017.
 */
public class Enq2OfferRoutes extends FacadeRestDslToSqsAsync {

    protected static final Logger LOG = LogManager.getLogger(Enq2OfferRoutes.class);

    public static final String API_ID_PROSPECTS_REST = "enq-to-offer-vc-prospects-rest-service";

    public static final String ROUTE_ID_GET_PROSPECT_LIST_REST = "get-prospect-list-main";
    public static final String ROUTE_ID_GET_PROSPECT_LIST_DIRECT = "get-prospect-list-direct";
    public static final String ROUTE_ID_GET_PROSPECT_BY_ID_REST = "get-prospect-by-id-main";
    public static final String ROUTE_ID_GET_PROSPECT_BY_ID_DIRECT = "get-prospect-by-id-direct";
    public static final String ROUTE_ID_PUT_PROSPECT_BODY_REST = "put-prospect-by-id-main";
    public static final String ROUTE_ID_PUT_PROSPECT_BODY_DIRECT = "put-prospect-by-id-direct";

    public Enq2OfferRoutes() {
        super();
    }

    public Enq2OfferRoutes(CamelContext context) {
        super(context);
    }

    @Override
    public void configure() throws Exception {
        super.configure();
        LOG.info("Enq2OfferRoutes.configure() starting...");

        restConfiguration().component("netty4-http").scheme("{{enq2offer-v01-facade.https_protocol}}").bindingMode(RestBindingMode.auto)
                .dataFormatProperty("prettyPrint", "true")
                .host("localhost")
                .contextPath("/enq2offer01/v01").port("{{shared-container-port}}")
        ;

        LOG.info("Enq2OfferRoutes.configure() about to create restDSL...");

        // nb. StreamCaching is set to true/false in the blueprint.xml <camelContext>...
        rest("/prospects").id(API_ID_PROSPECTS_REST).description("Prospect REST service")
                //JSON only...
                .consumes("application/json").produces("application/json")
                .get("/{id}").id(ROUTE_ID_GET_PROSPECT_BY_ID_REST).description("Find Prospect by id").outType(Prospect.class)
                .param().name("id").type(RestParamType.path).description("The id of the user to get").dataType("int").endParam()
                .to(logInfoUri + "&marker=1_" + ROUTE_ID_GET_PROSPECT_BY_ID_REST)
                .to("direct:" + ROUTE_ID_GET_PROSPECT_BY_ID_DIRECT)
                .put().id(ROUTE_ID_PUT_PROSPECT_BODY_REST).description("Updates or create a prospect").type(Prospect.class)
                .param().name("body").type(RestParamType.body).description("The prospect to update or create").endParam()
                .to(logInfoUri + "&marker=1_" + ROUTE_ID_PUT_PROSPECT_BODY_REST)
                .to("{{enq2offer-v01-facade.enq2offer-v01-host-url-put-prospect}}")

                .get().id(ROUTE_ID_GET_PROSPECT_LIST_REST).description("Get all prospects").outTypeList(Prospect.class)
                .param().name("pagesize").type(RestParamType.query).defaultValue("10").allowableValues("10", "20", "50").endParam()
                .param().name("page").type(RestParamType.query).defaultValue("1").endParam()
                .to(logInfoUri + "&marker=1_" + ROUTE_ID_GET_PROSPECT_LIST_REST)
                .to("{{enq2offer-v01-facade.enq2offer-v01-host-url-get-prospects}}")
        ;

        from("direct:" + ROUTE_ID_GET_PROSPECT_BY_ID_DIRECT)
                .routeId(ROUTE_ID_GET_PROSPECT_BY_ID_DIRECT)
                .to(logDebugUri + "&marker=1_" + ROUTE_ID_GET_PROSPECT_BY_ID_DIRECT)
                .to("{{enq2offer-v01-facade.enq2offer-v01-host-url-get-prospect}}")
        ;

        LOG.info("Enq2OfferRoutes.configure() completed.");

    }
}
