package tests.schoolTests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthTestSchool {

    @Test
    public void testAuth() {
        Map<String, String> data = new HashMap<>();
        data.put("email", "vinkotov@example.com");
        data.put("password", "1234");
        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        int userID = response.jsonPath()
                .get("user_id");
        String cookie = response.getCookie("auth_sid");
        String token = response.getHeader("x-csrf-token");

        Response secResponse = RestAssured.given()
                .cookie("auth_sid", cookie)
                .header("x-csrf-token", token)
                .get("https://playground.learnqa.ru/api/user/auth")
                .andReturn();
        int userID__ = secResponse.jsonPath().get("user_id");
        assertEquals(userID, userID__, "WRONG ID!!!");
    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testNegativeAuth(String condition) {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response response = RestAssured
                .given()
                .body(authData)
                .when()
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        Map<String, String> cookies = response.getCookies();
        Headers headers = response.getHeaders();

        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");
        if (condition.equals("cookie")) {
            spec.cookie("auth_sid", cookies.get("auth_sid"));
        } else if (condition.equals("headers")) {
            spec.header("x-csrf-token", headers.get("x-csrf-token"));
        } else {
            throw new IllegalArgumentException("Condition value is known: " + condition);
        }
        JsonPath responseForCheck = spec.get()
                .jsonPath();
        assertEquals(0, responseForCheck.getInt("user_id"), "User id should be 0");
    }
}