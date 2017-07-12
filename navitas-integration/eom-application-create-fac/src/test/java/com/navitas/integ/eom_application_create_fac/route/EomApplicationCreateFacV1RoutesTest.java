package com.navitas.integ.eom_application_create_fac.route;

import com.google.gson.Gson;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.navitas.integ.model.v01.applications.*;
import com.navitas.integ.common.test.blueprint.CamelBlueprintTestSupportHelper;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;

import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Date;

import static com.navitas.integ.eom_application_create_fac.route.EomApplicationCreateFacV1Routes.*;

/**
 * Created by user on 24/06/2017.
 */
public class EomApplicationCreateFacV1RoutesTest extends CamelBlueprintTestSupportHelper {

    private final static Logger LOG = LogManager.getLogger(EomApplicationCreateFacV1RoutesTest.class);

    private final static String BLUEPRINT_DESCRIPTOR = "OSGI-INF/blueprint/blueprint.xml";
    private final String LOG_DEBUG_URI = "log:" + this.getClass().getCanonicalName() + "?level=DEBUG&showProperties=true&showException=true&showBody=true";

    @Override
    protected String getBlueprintDescriptor() {
        return BLUEPRINT_DESCRIPTOR;
    }

    @Test
    public void T050_getProspectById_whenReceiveValidRequest_thenPassToHostAndReturnResponseJson() throws Exception {
        LOG.info("T050_getProspectById_whenReceiveValidRequest_thenPassToHostAndReturnResponseJson");

        String mockProspectResponse = generateTestApplicationStr();

        final RouteDefinition routeGetById = context.getRouteDefinition(ROUTE_ID_GET_APPLICATION_BY_ID_DIRECT);
        routeGetById.adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() {
//                replaceFromWith("direct:start");
                interceptSendToEndpoint("http*").skipSendToOriginalEndpoint()
                        .to("mock:hostRequest")
                        .setExchangePattern(ExchangePattern.InOut)
                        .setBody().constant(mockProspectResponse)
                ;
                weaveAddLast().to("mock:clientResponse");
            }

            ;
        });

        context.start();
        debugContext("after start");

        MockEndpoint mockHostRequestEndpoint = getMockEndpoint("mock:hostRequest");
        MockEndpoint mockClientResponseEndpoint = getMockEndpoint("mock:clientResponse");

        mockHostRequestEndpoint.expectedMessageCount(1);
        mockClientResponseEndpoint.expectedMessageCount(1);

        LOG.debug("XXXX About to send exchange. Template is " + template.getClass().getCanonicalName());
        Exchange exchange = template.request(
//                "direct:start" ,
                "direct:"+ROUTE_ID_GET_APPLICATION_BY_ID_DIRECT,
                ex -> {
                    ex.getIn().setHeader("id", "123");
                    ex.getIn().setHeader("accept", "application/json");
                    ex.getIn().setHeader("mary-pet", "Lamb");
                    ex.getIn().setBody(null);
                });
        assertExchangeOk(exchange);
        assertMockEndpointsSatisfied();
        Object responseO = exchange.getOut().getBody();
        LOG.debug("responseO.class="+(responseO==null?"null":responseO.getClass().getCanonicalName()));
        LOG.debug("responseO.toStr="+(responseO==null?"null":responseO.toString()));

        Object pojo = Configuration.defaultConfiguration().jsonProvider().parse(responseO.toString());
        assertEquals("Application.Applicant.Person.PersonIdentifier Wrong",
                "person124",
                JsonPath.read(pojo, "$.applicant.person.personIdentifier"));

        context.stop();
    }

    private String generateTestApplicationStr() {
        Application application = generateTestApplication();

        Gson gson = new Gson();
        return gson.toJson(application);
// swagger codegen-generated classes marshall invalid JSON!!!       return application.toString();

    }

    private Application generateTestApplication() {

        // Applicant
        PersonContact appPersonContact = new PersonContact();
        appPersonContact.setHomePhone("+123123123123");
        appPersonContact.setMobile("+614444888888");
        appPersonContact.setPersonalEmailAddress("fred@nurgler.com.cn");

        PersonHomeAddress appPersonHomeAddress = new PersonHomeAddress();
        appPersonHomeAddress.setAddressline1("42 Fairycake Lane");
        appPersonHomeAddress.setLocality("Shenzen");
        appPersonHomeAddress.setCountry("China");
        appPersonHomeAddress.setPostalCode("29384");
        appPersonHomeAddress.setRegion("Guandong Province");

        DateTime appBirthDate = DateTime.parse("1999-12-28T00:00:00.000+08:00");
        PersonDemographic appPersonDemographic = new PersonDemographic();
        appPersonDemographic.setCoutnryOfBirth("CN");
        appPersonDemographic.setDateOfBirth(appBirthDate.toDate());
        appPersonDemographic.setFamilyName("Boon");
        appPersonDemographic.setGivenNames("Mak Yi");
        appPersonDemographic.setPreferredName("Mel");
        appPersonDemographic.setSex(PersonDemographic.SexEnum.UNKNOWN);

        Person applicantPerson = new Person();
        applicantPerson.setPersonIdentifier("person124");
        applicantPerson.setContact(appPersonContact);
        applicantPerson.setHomeAddress(appPersonHomeAddress);
        applicantPerson.setDemographic(appPersonDemographic);

        Applicant applicant = new Applicant();
        applicant.setCitzenship("CN");
        applicant.setCountryOfOrigin("CN");
        applicant.setPerson(applicantPerson);

        Agent agent = new Agent();
        agent.setAgentCode("agent43203");
        agent.setAgentCountry("CN");

        EnglishTestTestScores englishTestTestScores = new EnglishTestTestScores();
        englishTestTestScores.setListeningScore("C");
        englishTestTestScores.setReadingScore("A");
        englishTestTestScores.setSpeakingScore("B");
        englishTestTestScores.setWritingScore("B");
        englishTestTestScores.setOverallScore("B");

        EnglishTest englishTest = new EnglishTest();
        englishTest.setInstitution("Guandong District English School");
        englishTest.setTestReferenceNumber("et1234");
        englishTest.setTestScores(englishTestTestScores);
        englishTest.setTestType("Nice");

        Passport passport = new Passport();
        passport.setPassportExpiry("20230812"); // funny it's not a date!
        passport.setPassportNumber("XYZ987654321");

        HighSchool highSchool = new HighSchool();
        highSchool.setCountry("CN");
        highSchool.setName("Shenzen High School");
        highSchool.setPostalCode("987654");
        highSchool.setRegion("Guandong Province");

        PriorLearning priorLearning = new PriorLearning();
        priorLearning.setHighestAcademicQualification("Phd. Nuclear Physics");
        priorLearning.setInstitution(highSchool);
        priorLearning.setYearCommenced("1");
        priorLearning.setYearCompleted("12");

        Application application = new Application();
        application.setApplicant(applicant);
        application.setAgent(agent);
        application.setEnglishTest(englishTest);
        application.setPassport(passport);
        application.setPriorLearning(priorLearning);
        return application;
    }
}
