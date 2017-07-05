package com.navitas.ospoc.enq2offer01.route;

import com.navitas.ospoc.enq2offer01.model.Prospect;
import com.navitas.ospoc.common.test.blueprint.CamelBlueprintTestSupportHelper;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.FromDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.rest.VerbDefinition;
import org.apache.camel.spi.InterceptStrategy;
import org.junit.Ignore;
import org.junit.Test;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import static com.navitas.ospoc.enq2offer01.route.Enq2OfferRoutes.*;

/**
 * Created by user on 24/06/2017.
 */
public class Enq2OfferProspectRouteTest extends CamelBlueprintTestSupportHelper {

    private final static Logger LOG = LogManager.getLogger(Enq2OfferProspectRouteTest.class);

    private final static String BLUEPRINT_DESCRIPTOR = "OSGI-INF/blueprint/blueprint.xml";
    private final String LOG_DEBUG_URI = "log:" + this.getClass().getCanonicalName() + "?level=DEBUG&showProperties=true&showException=true&showBody=true";

    @Override
    protected String getBlueprintDescriptor() {
        return BLUEPRINT_DESCRIPTOR;
    }

    @Test
    public void T050_getProspectById_whenReceiveValidRequest_thenPassToHostAndReturnResponseJson() throws Exception {
        LOG.info("T050_getProspectById_whenReceiveValidRequest_thenPassToHostAndReturnResponseJson");

        String mockProspectResponse = generateTestProspectStr();

        debugContext("before get...");
        final RouteDefinition routeGetById = context.getRouteDefinition(ROUTE_ID_GET_PROSPECT_BY_ID_DIRECT);
        routeGetById.adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() {
                replaceFromWith("direct:start");
                interceptSendToEndpoint("http*").skipSendToOriginalEndpoint()
                        .to("mock:hostRequest").setExchangePattern(ExchangePattern.InOut).to(LOG_DEBUG_URI + "&marker=before")
                        .setBody().constant(mockProspectResponse).to(LOG_DEBUG_URI + "&marker=after");
                weaveAddLast().to("mock:clientResponse");
            }

            ;
        });

        LOG.debug("XXXX About to start context...");
        context.start();
        debugContext("after start");

        MockEndpoint mockHostRequestEndpoint = getMockEndpoint("mock:hostRequest");
        MockEndpoint mockClientResponseEndpoint = getMockEndpoint("mock:clientResponse");

        mockHostRequestEndpoint.expectedMessageCount(1);
        mockClientResponseEndpoint.expectedMessageCount(1);

        LOG.debug("XXXX About to send exchange. Template is " + template.getClass().getCanonicalName());
        Exchange exchange = template.request(
                "direct:start" /*+ ROUTE_ID_GET_PROSPECT_BY_ID_DIRECT*/, ex -> {
                    ex.getIn().setHeader("id", "123");
                    ex.getIn().setHeader("accept", "application/json");
                    ex.getIn().setHeader("mary-pet", "Lamb");
                    ex.getIn().setBody(null);
                });
        assertExchangeOk(exchange);
        Object responseO = exchange.getOut().getBody();
        LOG.debug("responseO="+(responseO==null?"null":responseO.getClass().getCanonicalName()));
        assertEquals("ResponseO wrong!","class Prospect {\n" +
                "    id: p123\n" +
                "    firstName: Laura\n" +
                "    lastName: Gravvertay\n" +
                "    email: heavy.stuff@newton.com.gz\n" +
                "    phone: null\n" +
                "    prospectStatus: 3\n" +
                "}",responseO.toString());
        assertMockEndpointsSatisfied();

        context.stop();
    }

    private String generateTestProspectStr() {
        Prospect prospect = generateTestProspect();
        return prospect.toString();

    }

    private Prospect generateTestProspect() {
        Prospect prospect = new Prospect();
        prospect.setId("p123");
        prospect.setFirstName("Laura");
        prospect.setLastName("Gravvertay");
        prospect.setEmail("heavy.stuff@newton.com.gz");
        prospect.setProspectStatus(3);
        return prospect;
    }
}
