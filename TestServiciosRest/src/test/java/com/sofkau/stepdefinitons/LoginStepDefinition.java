package com.sofkau.stepdefinitons;

import com.sofkau.models.Response;
import com.sofkau.models.ResponseLogin;
import com.sofkau.models.User;
import com.sofkau.setup.ApiSetUp;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import org.apache.http.HttpStatus;

import static com.sofkau.questions.ReturnLoginSuccesfulJsonResponse.returnLoginSuccesfulJsonResponse;
import static com.sofkau.tasks.DoPost.doPost;
import static com.sofkau.utils.ReqresResources.LOGIN_SUCCESSFUL_RESOURCE;
import static com.sofkau.utils.ReqresResources.REQRES_BASE_URL;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginStepDefinition extends ApiSetUp {

    private User user_login = new User();

    @Given("the user is in the login page")
    public void theUserIsInTheLoginPage() {
        setUp(REQRES_BASE_URL.getValue());
    }

    @When("the user send a login request with the {string} and the {string}")
    public void theUserSendALoginRequestWithTheAndThe(String email, String password) {
        user_login.setEmail(email);
        user_login.setPassword(password);
        actor.attemptsTo(
            doPost()
                    .withTheResource(LOGIN_SUCCESSFUL_RESOURCE.getValue())
                    .andTheRequestBody(user_login)
        );
    }

    @Then("the user see a status response code and an id with a token")
    public void theUserSeeAStatusResponseCodeAndAnIdWithAToken() {
        ResponseLogin actualResponse = returnLoginSuccesfulJsonResponse().answeredBy(actor);
        System.out.println(SerenityRest.lastResponse().body().asString());
        actor.should(
                seeThatResponse("El codigo de respuesta es: " + HttpStatus.SC_OK,
                        response -> response.statusCode(200)),
                seeThat("Retorna información",
                        act -> actualResponse, notNullValue())
        );
    }
}