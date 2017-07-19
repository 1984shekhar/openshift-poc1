package com.navitas.integ.common.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesUtils {

    private static final String CONFIG_PATH = "etc/navitas-common.cfg";

    private static final Logger LOG = LoggerFactory.getLogger(PropertiesUtils.class);

    public static Properties loadProperties() {
        return loadProperties(PropertiesUtils.class.getClassLoader().getResourceAsStream(CONFIG_PATH));
    }

    public static Properties loadProperties(InputStream is) {
        Properties properties = new Properties();
        try {
            properties.load(is);
            if (LOG.isDebugEnabled()) {
                LOG.debug("loadProperties - loaded just from ImageStream: "+properties.toString());
            }
        } catch (IOException e) {
            LOG.error("loadProperties(is) - exception loading from inputStream: "+e.toString());
        }
        properties.putAll(System.getProperties());
        return properties;
    }

    public static String getProperty(Properties props, String propName) {
        if (props == null) {
            LOG.warn("getProperty(Properties props, String "+propName+") - Properties props was null!  Returning null property value.");
            return null;
        } else {
            String propVal = (String) props.get(propName);
            if (propVal == null) {
                LOG.warn("getProperty(props," + propName + ") - property not found.  Returning null property value.");
            }
            return propVal;
        }
    }


    public static String getProperty(Properties props, String propName, String defaultValue) {
        if (props == null) {
            LOG.warn("getProperty(Properties props, String "+propName+") - Properties props was null!  Returning defaultValue.");
            return defaultValue;
        } else {
            String propVal = (String) props.get(propName);
            if (propVal == null) {
                LOG.warn("getProperty(props," + propName + ") - property not found.  Returning defaultValue.");
                return defaultValue;
            }
            return propVal;
        }
    }

}
