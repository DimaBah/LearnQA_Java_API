package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static lib.StringConstants.KEY_AUTH_COOKIE;
import static lib.StringConstants.KEY_AUTH_TOKEN;

public class ApiCoreRequests {

    @Step("Make a GET-request to: {0}")
    public Response makeGetRequest(String url) {
        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request to: {0} with token and auth cookie")
    public Response makeGetRequest(String url,
                                   String token,
                                   String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header(KEY_AUTH_TOKEN.toString(), token))
                .cookie(KEY_AUTH_COOKIE.toString(), cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request to: {0} with auth cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie(KEY_AUTH_COOKIE.toString(), cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request to: {0} with token only")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header(KEY_AUTH_TOKEN.toString(), token))
                .get(url)
                .andReturn();
    }

    @Step("Make a POST-request to: {0}")
    public Response makePostRequest(String url,
                                    Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();

    }

    @Step("Make a PUT-request to: {0} with token, auth cookie and new data")
    public Response makePutRequest(String url,
                                   String token,
                                   String cookie,
                                   Map<String, String> editData) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header(KEY_AUTH_TOKEN.toString(), token))
                .cookie(KEY_AUTH_COOKIE.toString(), cookie)
                .body(editData)
                .put(url)
                .andReturn();
    }

    @Step("Make a PUT-request to: {0} with new data")
    public Response makePutRequest(String url,
                                   Map<String, String> editData) {
        return given()
                .filter(new AllureRestAssured())
                .body(editData)
                .put(url)
                .andReturn();
    }

    @Step("Make a DELETE-request to: {0} with token and auth cookie.")
    public Response makDeleteRequest(String url,
                                     String token,
                                     String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header(KEY_AUTH_TOKEN.toString(), token))
                .cookie(KEY_AUTH_COOKIE.toString(), cookie)
                .delete(url)
                .andReturn();
    }
}