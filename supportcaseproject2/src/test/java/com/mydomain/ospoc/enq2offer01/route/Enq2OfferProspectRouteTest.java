package com.mydomain.ospoc.enq2offer01.route;

import com.mydomain.ospoc.enq2offer01.model.Prospect;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.FromDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.rest.VerbDefinition;
import org.apache.camel.spi.InterceptStrategy;
import org.junit.Test;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import static com.mydomain.ospoc.enq2offer01.route.TestRoutes.*;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.osgi.framework.BundleContext;

import java.io.IOException;
import java.util.Properties;


/**
 * Created by user on 24/06/2017.
 */
public class Enq2OfferProspectRouteTest extends CamelBlueprintTestSupport {

    private final static Logger LOG = LogManager.getLogger(Enq2OfferProspectRouteTest.class);

    private final static String BLUEPRINT_DESCRIPTOR = "OSGI-INF/blueprint/blueprint.xml";
    private final String LOG_DEBUG_URI = "log:" + this.getClass().getCanonicalName() + "?level=DEBUG&showProperties=true&showException=true&showBody=true";
    private String testConfigPath = "mydomain.common.cfg"; // ...relative to src/test/resources

    @Override
    protected String getBlueprintDescriptor() {
        return BLUEPRINT_DESCRIPTOR;
    }


    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    protected boolean expectBlueprintContainerReloadOnConfigAdminUpdate() {
        return true;
    }

    /**
     * Skip felix fileinstall, whatever that is, to prevent half a million lines of:
     * "In main loop, we have serious trouble: java.lang.NullPointerException java.lang.NullPointerException
     * at org.apache.felix.fileinstall.internal.DirectoryWatcher.run(DirectoryWatcher.java:303)"
     * @return
     */
    @Override
    protected String getBundleFilter() {
        return "(!(Bundle-SymbolicName=org.apache.felix.fileinstall))";
    }

    @Override
    public boolean isCreateCamelContextPerClass() {
        return true;
    }


    @Override
    protected Properties useOverridePropertiesWithPropertiesComponent() {
        Properties properties = new Properties();
        LOG.debug("useOverridePropertiesWithPropertiesComponent starting.  This class="+getClass().getCanonicalName());
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(testConfigPath));
            if (properties == null) {
                LOG.error("useOverridePropertiesWithPropertiesComponent: properties is null!  Cannot load (.../test/resources/)"+ testConfigPath);
            } else {
                LOG.info("useOverridePropertiesWithPropertiesComponent: properties="+properties);
            }
        } catch (IOException e) {
            LOG.error("useOverridePropertiesWithPropertiesComponent: Config file at (.../test/resources/)"+ testConfigPath +" couldn't be loaded: "+e.toString());
            return null;
        }
        return properties;
    }


    @Override
    protected BundleContext createBundleContext() throws Exception {
        System.setProperty("org.apache.aries.blueprint.synchronous","true"); // see https://access.redhat.com/solutions/640943
        return super.createBundleContext();
    }

    protected void debugContext(String comment) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("CamelBlueprintTestSupportHelper.debugContext: "+(comment==null?"":comment));
            for (RouteDefinition rd : context.getRouteDefinitions()) {
                LOG.debug("RouteDefinition in Context: " + rd.getId());
                if (rd.getRestDefinition() != null) {
                    LOG.debug("- Has REST definition: path=" + rd.getRestDefinition().getPath());
                    for (VerbDefinition vd : rd.getRestDefinition().getVerbs()) {
                        LOG.debug("-   Verb " + vd.asVerb() + ": " + vd.getUri());
                    }
                }
                for (FromDefinition fd : rd.getInputs()) {
                    LOG.debug("- From " + fd.getEndpointUri());
                }
                for (InterceptStrategy is : rd.getInterceptStrategies()) {
                    LOG.debug("- Intercepts " + is.toString());
                }
            }
        }

    }
    protected void assertExchangeOk(Exchange exchange) {
        assertNotNull("Returned exchange was null!!!",exchange);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Exchange Class=" + exchange.getClass().getCanonicalName());
            LOG.debug("Exchange FromRID=" + (exchange.getFromRouteId() == null ? "null" : exchange.getFromRouteId()));
            LOG.debug("Exchange Exception=" + (exchange.getException() == null ? "null" : exchange.getException().toString()));
            LOG.debug("Exchange FromEndPt=" + (exchange.getFromEndpoint() == null ? "null" : exchange.getFromEndpoint()));
            LOG.debug("Exchange InHdrs=" + (exchange.getIn() == null ? "null" : exchange.getIn().getHeaders().toString()));
            LOG.debug("Exchange Properties=" + (exchange.getProperties() == null ? "null" : exchange.getProperties().toString()));
        }
        if (exchange.getException() != null) {
            fail("Exchange Exception="+exchange.getException().toString());
        }
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
