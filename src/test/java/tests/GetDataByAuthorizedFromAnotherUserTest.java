package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static lib.StringConstants.*;

@Epic("Reading data cases")
@Feature("Reading data")
@Story("Reading data from another user cases")
@Owner("Dzmitry Bakhmatski")
public class GetDataByAuthorizedFromAnotherUserTest extends BaseTestCase {
    private static final String URL_API_USER_2 = String.format(URL_API_USER_ID_PATTERN.toString(), 2);
    String cookie;
    String header;

    @Test
    @Description("This test tries to get data from another user with non authorized request")
    @DisplayName("Test negative reading data from another user with non authorized request")
    public void testUserGetDataFromAnotherUserWithNonAuthRequest() {
        //Authorization by new user
        loginAndAuthorizeByNewUser();

        Response responseUserData = apiCoreRequests.makeGetRequest(URL_API_USER_2);

        Assertions.assertJsonHasField(responseUserData, KEY_USERNAME);
        Assertions.assertJsonHasNotField(responseUserData, KEY_EMAIL);
        Assertions.assertJsonHasNotField(responseUserData, KEY_FIRSTNAME);
        Assertions.assertJsonHasNotField(responseUserData, KEY_LASTNAME);
        Assertions.assertJsonHasNotField(responseUserData, KEY_PASSWORD);
        Assertions.assertJsonHasNotField(responseUserData, KEY_ID);
    }

    @Test
    @Description("This test tries to get data from another user with authorized request")
    @DisplayName("Test negative reading data from another user with authorized request")
    public void testUserGetDataFromAnotherUserWithAuthRequest() {
        //Authorization by new user
        loginAndAuthorizeByNewUser();

        Response responseUserData = apiCoreRequests.makeGetRequest(URL_API_USER_2, header, cookie);

        Assertions.assertJsonHasField(responseUserData, KEY_USERNAME);
        Assertions.assertJsonHasNotField(responseUserData, KEY_EMAIL);
        Assertions.assertJsonHasNotField(responseUserData, KEY_FIRSTNAME);
        Assertions.assertJsonHasNotField(responseUserData, KEY_LASTNAME);
        Assertions.assertJsonHasNotField(responseUserData, KEY_PASSWORD);
        Assertions.assertJsonHasNotField(responseUserData, KEY_ID);
    }

    private void loginAndAuthorizeByNewUser() {
        Map<String, String> userData = DataGenerator.getNewUserData();

        apiCoreRequests.makePostRequest(URL_API_USER.toString(), userData);

        Map<String, String> authData = new HashMap<>();
        authData.put(KEY_EMAIL.toString(), userData.get(KEY_EMAIL.toString()));
        authData.put(KEY_PASSWORD.toString(), userData.get(KEY_PASSWORD.toString()));

        Response loginResponse = apiCoreRequests.makePostRequest(URL_API_USER_LOGIN.toString(), authData);

        cookie = getCookie(loginResponse, KEY_AUTH_COOKIE.toString());
        header = getHeader(loginResponse, KEY_AUTH_TOKEN.toString());
    }
}