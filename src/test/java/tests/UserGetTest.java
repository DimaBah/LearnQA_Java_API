package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_LASTNAME = "lastName";
    private static final String URL_USER_LOGIN = "https://playground.learnqa.ru/api/user/login";
    private static final String URL_USER_2 = "https://playground.learnqa.ru/api/user/2";
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testUserGetDataNotAuth() {
        Response responseUserData = RestAssured
                .get(URL_USER_2)
                .andReturn();

        System.out.println(responseUserData.asString());
        Assertions.assertJsonHasField(responseUserData, KEY_USERNAME);
        Assertions.assertJsonHasNotField(responseUserData, KEY_EMAIL);
        Assertions.assertJsonHasNotField(responseUserData, KEY_FIRSTNAME);
        Assertions.assertJsonHasNotField(responseUserData, KEY_LASTNAME);
        Assertions.assertJsonHasNotField(responseUserData, KEY_PASSWORD);
    }

    @Test
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put(KEY_EMAIL, "vinkotov@example.com");
        authData.put(KEY_PASSWORD, "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest(URL_USER_LOGIN, authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequests.makeGetRequest(URL_USER_2, header, cookie);

        Assertions.assertJsonHasField(responseUserData, KEY_USERNAME);
        Assertions.assertJsonHasField(responseUserData, KEY_EMAIL);
        Assertions.assertJsonHasField(responseUserData, KEY_FIRSTNAME);
        Assertions.assertJsonHasField(responseUserData, KEY_LASTNAME);

        String[] expectedFields = {KEY_USERNAME, KEY_EMAIL, KEY_FIRSTNAME, KEY_LASTNAME};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }
}