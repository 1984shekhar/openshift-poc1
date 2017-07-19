package com.navitas.integ.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by user on 21/06/2017.
 */
public class LancesClassToTestProperties {
    private String propertyString1;
    private String propertyString2;

    private static final Logger LOG = LoggerFactory.getLogger(LancesClassToTestProperties.class);

    public void LancesClassToTestProperties() {}

    public void LancesClassToTestProperties(String p1, String p2) {
        this.propertyString1 = p1;
        this.propertyString2 = p2;
        LOG.info("Constructor: p1="+p1+",p2="+p2+".");
    }

    public String getPropertyString1() {
        return propertyString1;
    }

    public void setPropertyString1(String propertyString1) {
        this.propertyString1 = propertyString1;
        LOG.info("setPropertyString1: propertyString1="+this.propertyString1+".");
    }

    public String getPropertyString2() {
        return propertyString2;
    }

    public void setPropertyString2(String propertyString2) {
        this.propertyString2 = propertyString2;
        LOG.info("setPropertyString2: propertyString2="+this.propertyString2+".");
    }

}
