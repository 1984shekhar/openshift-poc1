package com.navitas.integ.eom_create_application_v01_fac.route;

import com.navitas.integ.common.route.patterns.FacadeRestDslToSqsAsync;
import com.navitas.integ.model.v01.applications.Application;
import org.apache.camel.CamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by user on 22/06/2017.
 */
public class EomCreateApplicationFacRoutes extends FacadeRestDslToSqsAsync {

    protected static final Logger LOG = LoggerFactory.getLogger(EomCreateApplicationFacRoutes.class);

    public static final String API_ID_CREATE_APPLICATIONS_FAC_REST = "eom-create-application-fac-rest-service";

    public static final String ROUTE_ID_GET_PROSPECT_LIST_REST = "get-application-list-main";
    public static final String ROUTE_ID_GET_PROSPECT_LIST_DIRECT = "get-application-list-direct";
    public static final String ROUTE_ID_GET_APPLICATION_BY_ID_REST = "get-application-by-id-main";
    public static final String ROUTE_ID_GET_APPLICATION_BY_ID_DIRECT = "get-application-by-id-direct";
    public static final String ROUTE_ID_POST_APPLICATION_BODY_REST = "put-application-by-id-main";
    public static final String ROUTE_ID_POST_APPLICATION_BODY_DIRECT = "put-application-by-id-direct";

    public EomCreateApplicationFacRoutes() {
        super();
    }

    public EomCreateApplicationFacRoutes(CamelContext context) {
        super(context);
    }

    @Override
    public void configure() throws Exception {
        super.configure();
        LOG.info("EomCreateApplicationFacRoutes.configure() starting...");

        restConfiguration().component("netty4-http").scheme("{{enq2offer-v01-facade.https_protocol}}").bindingMode(RestBindingMode.auto)
                .dataFormatProperty("prettyPrint", "true")
                .host("localhost")
                .contextPath("rest/v2").port("{{shared-container-port}}")
        ;

        LOG.info("EomCreateApplicationFacRoutes.configure() about to create restDSL...");

        // nb. StreamCaching is set to true/false in the blueprint.xml <camelContext>...
        rest("/application").id(API_ID_CREATE_APPLICATIONS_FAC_REST).description("Create Application REST service")
                .consumes("application/json").produces("application/json")

                .get("/{id}").id(ROUTE_ID_GET_APPLICATION_BY_ID_REST).description("Find Create Application by id").outType(Application.class)
                .param().name("id").type(RestParamType.path).description("The id of the user to get").dataType("int").endParam()
                .route()
                .to(logInfoUri + "&marker=1_" + ROUTE_ID_GET_APPLICATION_BY_ID_REST)
                .to("direct:" + ROUTE_ID_GET_APPLICATION_BY_ID_DIRECT)
                .endRest()

                .put().id(ROUTE_ID_POST_APPLICATION_BODY_REST).description("Updates or create a application").type(Application.class)
                .param().name("body").type(RestParamType.body).description("The application to update or create").endParam()
                .route()
                .to(logInfoUri + "&marker=1_" + ROUTE_ID_POST_APPLICATION_BODY_REST)
                .to("{{enq2offer-v01-facade.enq2offer-v01-host-url-put-application}}")
                .endRest()

                .get().id(ROUTE_ID_GET_PROSPECT_LIST_REST).description("Get all applications").outTypeList(Application.class)
                .param().name("pagesize").type(RestParamType.query).defaultValue("10").allowableValues("10", "20", "50").endParam()
                .param().name("page").type(RestParamType.query).defaultValue("1").endParam()
                .route()
                .to(logInfoUri + "&marker=1_" + ROUTE_ID_GET_PROSPECT_LIST_REST)
                .to("{{enq2offer-v01-facade.enq2offer-v01-host-url-get-application}}")
                .endRest()
        ;

        from("direct:" + ROUTE_ID_GET_APPLICATION_BY_ID_DIRECT)
                .routeId(ROUTE_ID_GET_APPLICATION_BY_ID_DIRECT)
                .to(logDebugUri + "&marker=1_" + ROUTE_ID_GET_APPLICATION_BY_ID_DIRECT)
                .to("{{enq2offer-v01-facade.enq2offer-v01-host-url-get-application}}")
        ;

        LOG.info("EomCreateApplicationFacRoutes.configure() completed.");

    }
}
