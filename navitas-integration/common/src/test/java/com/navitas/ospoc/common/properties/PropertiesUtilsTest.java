package com.navitas.ospoc.common.properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

/**
 * Created by Lance Bryant on 03/07/2017.
 */
public class PropertiesUtilsTest {

    private static final Logger LOG = LogManager.getLogger(PropertiesUtilsTest.class);

    private static final String TEST_CONFIG_PATH = "etc/PropertiesUtilsTest-navitas-common.cfg";

    @Test
    public void TC050_load_whenPropertyFileFound_thenReturnsProperties() {
        LOG.info("TC050_load_whenPropertyFileFound_thenReturnsProperties");

        Properties props = PropertiesUtils.loadProperties(PropertiesUtilsTest.class.getClassLoader().getResourceAsStream(TEST_CONFIG_PATH));
        Assert.assertNotNull("loadProperties() returned null!", props);
        Assert.assertTrue("Loaded properties had " + props.size() + " entries!", props.size() != 0);
    }

    @Test
    public void TC060_getProperty_whenPropertyFound_thenReturnsValueString() {
        LOG.info("TC060_getProperty_whenPropertyFound_thenReturnsValueString");

        Properties props = PropertiesUtils.loadProperties(PropertiesUtilsTest.class.getClassLoader().getResourceAsStream(TEST_CONFIG_PATH));
        Assert.assertNotNull("loadProperties() returned null!", props);
        String resultStr = PropertiesUtils.getProperty(props, "test.prop1");
        Assert.assertNotNull("getProperty returned null!", resultStr);
        Assert.assertEquals("Property value was wrong!", "Mary had a little lamb", resultStr);
    }

    @Test
    public void TC061_getProperty_whenPropertyNotFound_thenReturnsNull() {
        LOG.info("TC061_getProperty_whenPropertyNotFound_thenReturnsNull");

        Properties props = PropertiesUtils.loadProperties(PropertiesUtilsTest.class.getClassLoader().getResourceAsStream(TEST_CONFIG_PATH));
        Assert.assertNotNull("loadProperties() returned null!", props);
        String resultStr = PropertiesUtils.getProperty(props, "absent.prop.name1");
        Assert.assertNull("getProperty returned a value!", resultStr);
    }

    @Test
    public void TC062_getProperty_whenPropertyNotFoundButDefaultSet_thenReturnsDefault() {
        LOG.info("TC062_getProperty_whenPropertyNotFoundButDefaultSet_thenReturnsDefault");

        Properties props = PropertiesUtils.loadProperties(PropertiesUtilsTest.class.getClassLoader().getResourceAsStream(TEST_CONFIG_PATH));
        Assert.assertNotNull("loadProperties() returned null!", props);
        String resultStr = PropertiesUtils.getProperty(props, "absent.prop.name1", "defaultValue1");
        Assert.assertNotNull("getProperty returned null!", resultStr);
        Assert.assertEquals("Property value was wrong!", "defaultValue1", resultStr);
    }


    @Test
    public void TC063_getProperty_whenPropertyNotFoundAndDefaultNull_thenReturnsNull() {
        LOG.info("TC063_getProperty_whenPropertyNotFoundAndDefaultNull_thenReturnsNull");

        Properties props = PropertiesUtils.loadProperties(PropertiesUtilsTest.class.getClassLoader().getResourceAsStream(TEST_CONFIG_PATH));
        Assert.assertNotNull("loadProperties() returned null!", props);
        String resultStr = PropertiesUtils.getProperty(props, "absent.prop.name1", null);
        Assert.assertNull("getProperty returned a value!", resultStr);
    }
}