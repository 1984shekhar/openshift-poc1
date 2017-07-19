package com.navitas.integ.common.test.blueprint;

import com.navitas.integ.common.file.FileUtils;
import org.apache.camel.Exchange;
import org.apache.camel.model.FromDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.rest.VerbDefinition;
import org.apache.camel.spi.InterceptStrategy;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.osgi.framework.BundleContext;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Lance Bryant on 04/07/2017.
 */
public class CamelBlueprintTestSupportHelper extends CamelBlueprintTestSupport {

    private static final Logger LOG = LoggerFactory.getLogger(CamelBlueprintTestSupportHelper.class);

    private String testConfigPath = "navitas.common.cfg"; // ...relative to src/test/resources

    @Override
    protected String getBlueprintDescriptor() {
        return "((((Need to override getBlueprintDescriptor() in your test-class!))))";
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

    /**
     * returns the contents of a test-data input-file as a string
     * @param path
     * @return
     */
    protected String getTestFile(String path) {
        return FileUtils.readFile(getClass(),path);
    }

    /**
     * Sets the path to the test-config file if the test requires a non-standard name.
     * Default is test/resources/navitas.common.cfg
     * @param testConfigPath
     */
    protected void setTestConfigPath(String testConfigPath) {
        this.testConfigPath = testConfigPath;
    }
}
