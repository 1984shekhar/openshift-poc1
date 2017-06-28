package com.navitas.ospoc.enq2offer01.route;

import com.navitas.ospoc.enq2offer01.model.Prospect;
import org.apache.camel.*;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spi.Synchronization;
import org.apache.camel.spi.UnitOfWork;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Ignore;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by user on 24/06/2017.
 */
public class Enq2OfferProspectRouteTest extends CamelBlueprintTestSupport {

    private static final Logger LOG = LogManager.getLogger(Enq2OfferProspectRouteTest.class);
    static final String BLUEPRINT_DESCRIPTOR = "OSGI-INF/blueprint/blueprint.xml";
    static final String CONFIG_PATH = "navitas.common.cfg";

    @Override
    protected String getBlueprintDescriptor() {
        return BLUEPRINT_DESCRIPTOR;
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    protected Properties useOverridePropertiesWithPropertiesComponent() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("navitas.common.cfg"));
        } catch (IOException e) {
            LOG.error("Config path "+CONFIG_PATH+" couldn't be loaded: "+e.toString());
            return null;
        }
        return properties;
    }


    @Override
    protected BundleContext createBundleContext() throws Exception {
        System.setProperty("org.apache.aries.blueprint.synchronous","true"); // see https://access.redhat.com/solutions/640943
        return super.createBundleContext();
    }

    @Test
    @Ignore
    public void T050_getProspectById_whenReceiveValidRequest_thenPassToHostAndReturnResponseJson() throws Exception {
        LOG.info("T050_getProspectById_whenReceiveValidRequest_thenPassToHostAndReturnResponseJson");
        Prospect prospectResponse = new Prospect();
        prospectResponse.setId("p123");
        prospectResponse.setFirstName("Laura");
        prospectResponse.setLastName("Gravvertay");
        prospectResponse.setEmail("heavy.stuff@newton.com.gz");
        prospectResponse.setProspectStatus(3);
        String mockProspectResponse = prospectResponse.toString();
        LOG.info("canned response: "+mockProspectResponse);

        final RouteDefinition getByIdRoute = context.getRouteDefinition("get-prospect-by-id");
        assertNotNull("Route Definition is Null!",getByIdRoute);
        getByIdRoute.adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() {
//                replaceFromWith("direct:start");
                interceptSendToEndpoint("http*").skipSendToOriginalEndpoint()
                        .to("mock:hostRequest").setExchangePattern(ExchangePattern.InOut)
                        .setBody().constant(mockProspectResponse);
                weaveAddLast().to("mock:clientResponse");
            };
        });

        context.start();

        MockEndpoint mockHostRequestEndpoint = getMockEndpoint("mock:hostRequest");
        MockEndpoint mockClientResponseEndpoint = getMockEndpoint("mock:clientResponse");

        mockHostRequestEndpoint.expectedMessageCount(1);
        mockClientResponseEndpoint.expectedMessageCount(1);

        Exchange exchange = new DefaultExchange(context);
        template.send("http://localhost:8080/enq2offer01/v01/prospects/123", exchange);
//        String clientResponse = (String)template.requestBody("direct:start", "");

        assertMockEndpointsSatisfied();

//        assertNotNull("client-response is null!",clientResponse);

        context.stop();
    }

}
