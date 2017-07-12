package com.navitas.integ.common_awssqs.route;

import com.navitas.integ.common.route.patterns.FacadeRestDslToSqsAsync;
import com.navitas.integ.model.v01.applications.Application;
import org.apache.camel.CamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Navitas Adapter: Amazon Web Services - Simple Queue Service
 * Created by user on 22/06/2017.
 */
public class CommonAwsSqsV1Routes extends RouteBuilder {

    protected static final Logger LOG = LogManager.getLogger(CommonAwsSqsV1Routes.class);

    public static final String ROUTE_ID_SQS_QUEUE_SENDMSG = "common-aws-sqs-send-v1";
    public static final String ROUTE_ID_SQS_QUEUE_RECVMSG = "common-aws-sqs-recv-v1";

    @Override
    public void configure() throws Exception {
        super.configure();
        LOG.info("CommonAwsSqsV1Routes.configure() starting...");

        from("direct-vm:"+ROUTE_ID_SQS_QUEUE_SENDMSG)
                .id(ROUTE_ID_SQS_QUEUE_SENDMSG)

        restConfiguration().component("netty4-http")
                .scheme("{{eom-application-create-fac-inf0002.https_protocol}}")
                .bindingMode(RestBindingMode.auto)
                .dataFormatProperty("prettyPrint", "true")
                .host("localhost")
                .contextPath("restapi")
//                .port("{{shared-container-port}}")
        ;

        // nb. StreamCaching is set to true/false in the blueprint.xml <camelContext>...
        rest("/application")
                .id(API_ID_CREATE_APPLICATIONS_FAC_REST)
                .description("Create Application REST service")
                .consumes("application/json").produces("application/json")

                .get("/{id}").id(ROUTE_ID_GET_APPLICATION_BY_ID_REST).description("Find Create Application by id")
                .outType(Application.class)
                .param().name("id").type(RestParamType.path).description("The id of the user to get").dataType("int").endParam()
                .to(logInfoUri + "&marker=1_" + ROUTE_ID_GET_APPLICATION_BY_ID_REST)
                .to("direct:" + ROUTE_ID_GET_APPLICATION_BY_ID_DIRECT)
                
                .put().id(ROUTE_ID_PUT_APPLICATION_BODY_REST).description("Updates or create a application").type(Application.class)
                .param().name("body").type(RestParamType.body).description("The application to update or create").endParam()
                .to(logInfoUri + "&marker=1_" + ROUTE_ID_PUT_APPLICATION_BODY_REST)
                .to("direct:"+ROUTE_ID_PUT_APPLICATION_BODY_DIRECT)

                .get().id(ROUTE_ID_GET_APPLICATION_LIST_REST).description("Get all applications").outTypeList(Application.class)
                .param().name("pagesize").type(RestParamType.query).defaultValue("10").allowableValues("10", "20", "50").endParam()
                .param().name("page").type(RestParamType.query).defaultValue("1").endParam()
                .to(logInfoUri + "&marker=1_" + ROUTE_ID_GET_APPLICATION_LIST_REST)
                .to("direct:"+ROUTE_ID_GET_APPLICATION_LIST_DIRECT)
        ;

        from("direct://" + ROUTE_ID_GET_APPLICATION_BY_ID_DIRECT)
                .routeId(ROUTE_ID_GET_APPLICATION_BY_ID_DIRECT)
                .to(logDebugUri + "&marker=1_" + ROUTE_ID_GET_APPLICATION_BY_ID_DIRECT)
                .to("{{eom-application-create-fac-inf0002.get-by-id-v1-backend-endpoint}}")
        ;

        from("direct:" + ROUTE_ID_PUT_APPLICATION_BODY_DIRECT)
                .routeId(ROUTE_ID_PUT_APPLICATION_BODY_DIRECT)
                .to(logDebugUri + "&marker=1_" + ROUTE_ID_PUT_APPLICATION_BODY_DIRECT)
                .to("{{eom-application-create-fac-inf0002.put-body-v1-backend-endpoint}}")
        ;

        from("direct:" + ROUTE_ID_GET_APPLICATION_LIST_DIRECT)
                .routeId(ROUTE_ID_GET_APPLICATION_LIST_DIRECT)
                .to(logDebugUri + "&marker=1_" + ROUTE_ID_GET_APPLICATION_LIST_DIRECT)
                .to("{{eom-application-create-fac-inf0002.get-list-v1-backend-endpoint}}")
        ;

        LOG.info("EomApplicationCreateFacV1Routes.configure() completed.");

    }
}
