package org.miklosbarabas.demo.stepdefs;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.miklosbarabas.demo.repositories.PaymentAttributesRepository;
import org.miklosbarabas.demo.repositories.PaymentRepository;
import org.miklosbarabas.demo.services.PaymentTestBase;

import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

/**
 * Behaviour tests step definitions for {@link PaymentRepository} & {@link PaymentAttributesRepository}
 *
 * @author Miklos Barabas
 */
@Slf4j
@Ignore
public class PaymentStepDefinitions extends PaymentTestBase {
    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Before
    public void before() throws IOException {
        setup();
    }
    @After
    public void after() {
        clearDb();
    }


    @Given("a Payment resource exists with an ID of (.*)")
    public void a_payment_exists_with_id(String id){
        request = given().baseUri(HOST).port(PORT).basePath(PAYMENTS_PATH + id);
        log.info("GIVEN: \n URL: {}:{}{}{}", HOST, PORT, PAYMENTS_PATH, id);
    }

    @When("a user retrieves the Payment resource by ID")
    public void a_user_retrieves_the_payment_by_id(){
        response = request.when().get();
    }

    @Then("the status code is (\\d+)")
    public void verify_status_code(int statusCode){
        json = response.then().statusCode(statusCode);
    }

    @And("response includes the following$")
    public void response_equals(Map<String,String> responseFields){
        for (Map.Entry<String, String> field : responseFields.entrySet()) {
            if(StringUtils.isNumeric(field.getValue())){
                json.body(field.getKey(), equalTo(Integer.parseInt(field.getValue())));
            }
            else{
                json.body(field.getKey(), equalTo(field.getValue()));
            }
        }
    }

    @And("response includes the following in any order")
    public void response_contains_in_any_order(Map<String,String> responseFields){
        for (Map.Entry<String, String> field : responseFields.entrySet()) {
            if(StringUtils.isNumeric(field.getValue())){
                json.body(field.getKey(), containsInAnyOrder(Integer.parseInt(field.getValue())));
            }
            else{
                json.body(field.getKey(), containsInAnyOrder(field.getValue()));
            }
        }
    }

}

