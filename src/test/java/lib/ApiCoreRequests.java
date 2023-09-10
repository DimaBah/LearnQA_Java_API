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

    @Step("Make a GET-request")
    public Response makeGetRequest(String url) {
        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with token and auth cookie")
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

    @Step("Make a GET-request with auth cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie(KEY_AUTH_COOKIE.toString(), cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with token only")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header(KEY_AUTH_TOKEN.toString(), token))
                .get(url)
                .andReturn();
    }

    @Step("Make a POST-request")
    public Response makePostRequest(String url,
                                    Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();

    }

    @Step("Make a PUT-request with token, auth cookie and new data")
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
}