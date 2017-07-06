package com.navitas.it;

import com.navitas.integ.common.file.FileUtils;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;

import java.util.UUID;

@Test
public class DemoCustomerIT extends TestNGCitrusTestDesigner {

    // ===================================================================
    // Reference: http://www.citrusframework.org/reference/2.7/html/
    // ===================================================================

    // Endpoints configured in citrus-context.xml
    private static final String CUSTOMER_ENDPOINT = "customerEndpoint";

    private static final String MSG_NEW_CUSTOMER123 = FileUtils.readFile(DemoCustomerIT.class,"test-input/addCustomer123.xml");

    @CitrusTest
    public void rest_get_customer() {
        setRandomCorrelationId();

        http().client(CUSTOMER_ENDPOINT)
                .send()
                .get("/customerservice/customers/123")
                .contentType("application/xml")
                .accept("application/xml")
        ;
        http().client(CUSTOMER_ENDPOINT)
                .receive()
                .response(HttpStatus.OK)
                .schemaValidation(false)
                // for json payloads: .validate("$.user.name", "Penny")
                .xpath("*[local-name() = 'Customer']/*[local-name() = 'id']", "123")
                .xpath("//*[local-name() = 'name']", "Lance")
                // ...seems to be xpath 1.0 so "*:name" notation doesn't work :(
        ;
    }


    @CitrusTest
    public void rest_post_addCustomer() {
        setRandomCorrelationId();

        http().client(CUSTOMER_ENDPOINT)
                .send()
                .post("/customerservice/customers")
                .payload(MSG_NEW_CUSTOMER123)
                .contentType("application/xml")
                .accept("application/xml")
        ;
        http().client(CUSTOMER_ENDPOINT)
                .receive()
                .response(HttpStatus.OK)
                .schemaValidation(false)
                .xpath("//*[local-name() = 'name']", "Jack")
        ;
    }


    private void setRandomCorrelationId() {
        variable("correlationId", UUID.randomUUID().toString());
    }
}
