package tests.schoolTests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthTestSchool2 extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;

    @BeforeEach
    public void loginUser() {
        Map<String, String> data = new HashMap<>();
        data.put("email", "vinkotov@example.com");
        data.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        this.cookie = responseGetAuth.getCookie("auth_sid");
        this.header = responseGetAuth.header("x-csrf-token");
        this.userIdOnAuth = responseGetAuth.jsonPath()
                .get("user_id");
    }

    @Test
    public void testPositiveAuth() {
        Response responseCheckAuth = RestAssured.given()
                .cookie("auth_sid", this.cookie)
                .header("x-csrf-token", this.header)
                .get("https://playground.learnqa.ru/api/user/auth")
                .andReturn();
        int userIdOnCheck = responseCheckAuth.jsonPath()
                .getInt("user_id");

        assertTrue(userIdOnCheck > 0, "User id should be > 0");

        assertEquals(userIdOnAuth,
                userIdOnCheck,
                "WRONG ID!!!");
    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testNegativeAuth(String condition) {
        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");
        if (condition.equals("cookie")) {
            spec.cookie("auth_sid", this.cookie);
        } else if (condition.equals("headers")) {
            spec.header("x-csrf-token", this.header);
        } else {
            throw new IllegalArgumentException("Condition value is known: " + condition);
        }
        JsonPath responseForCheck = spec.get()
                .jsonPath();
        assertEquals(0, responseForCheck.getInt("user_id"), "User id should be 0");
    }
}