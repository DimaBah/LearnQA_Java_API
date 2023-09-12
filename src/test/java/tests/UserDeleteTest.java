package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static lib.StringConstants.*;

@Epic("Deleting data cases")
@Feature("Deleting user")
@Story("Positive and negative editing just created user data cases")
public class UserDeleteTest extends BaseTestCase {
    private String userId;
    private Map<String, String> newUserData;

    @Test
    public void testDeleteUserWithId_2() {
        //Login
        Map<String, String> authData = new HashMap<>();
        authData.put(KEY_EMAIL.toString(), "vinkotov@example.com");
        authData.put(KEY_PASSWORD.toString(), "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest(URL_API_USER_LOGIN.toString(), authData);

        String headerResponseGetAuth = getHeader(responseGetAuth, KEY_AUTH_TOKEN.toString());
        String cookieResponseGetAuth = getCookie(responseGetAuth, KEY_AUTH_COOKIE.toString());

        //Delete user
        String urlApiUserId = String.format(URL_API_USER_ID_PATTERN.toString(), 2);

        Response responseUserDelete = apiCoreRequests.makDeleteRequest(urlApiUserId, headerResponseGetAuth, cookieResponseGetAuth);

        //Check user
        Response responseUserData = apiCoreRequests.makeGetRequest(urlApiUserId, headerResponseGetAuth, cookieResponseGetAuth);

        Assertions.assertResponseCodeeEquals(responseUserDelete, "400");
        Assertions.assertResponseTextEquals(responseUserDelete, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
        Assertions.assertJsonByName(responseUserData, KEY_ID, "2");
    }

    @Test
    public void testDeleteJustCreatedUser() {
        //Generate new user
        createNewUser();

        //Login
        Map<String, String> authData = new HashMap<>();
        authData.put(KEY_EMAIL.toString(), newUserData.get(KEY_EMAIL.toString()));
        authData.put(KEY_PASSWORD.toString(), newUserData.get(KEY_PASSWORD.toString()));

        Response responseGetAuth = apiCoreRequests.makePostRequest(URL_API_USER_LOGIN.toString(), authData);

        String headerResponseGetAuth = getHeader(responseGetAuth, KEY_AUTH_TOKEN.toString());
        String cookieResponseGetAuth = getCookie(responseGetAuth, KEY_AUTH_COOKIE.toString());

        //Delete user
        String urlApiUserId = String.format(URL_API_USER_ID_PATTERN.toString(), userId);

        Response responseUserDelete = apiCoreRequests.makDeleteRequest(urlApiUserId, headerResponseGetAuth, cookieResponseGetAuth);

        //Check user
        Response responseUserData = apiCoreRequests.makeGetRequest(urlApiUserId, headerResponseGetAuth, cookieResponseGetAuth);

        Assertions.assertResponseCodeeEquals(responseUserDelete, "200");
        Assertions.assertResponseTextEquals(responseUserData, "User not found");
        Assertions.assertResponseCodeeEquals(responseUserData, "404");
    }

    @Test
    public void testDeleteJustCreatedUserWithAnotherUserAuthorization() {
        //Generate new user
        createNewUser();

        //Login as another user
        Map<String, String> authData = new HashMap<>();
        authData.put(KEY_EMAIL.toString(), "vinkotov@example.com");
        authData.put(KEY_PASSWORD.toString(), "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest(URL_API_USER_LOGIN.toString(), authData);

        String headerResponseGetAuth = getHeader(responseGetAuth, KEY_AUTH_TOKEN.toString());
        String cookieResponseGetAuth = getCookie(responseGetAuth, KEY_AUTH_COOKIE.toString());

        //Delete user
        String urlApiUserId = String.format(URL_API_USER_ID_PATTERN.toString(), userId);

        Response responseUserDelete = apiCoreRequests.makDeleteRequest(urlApiUserId, headerResponseGetAuth, cookieResponseGetAuth);

        //Check user
        Response responseUserData = apiCoreRequests.makeGetRequest(urlApiUserId, headerResponseGetAuth, cookieResponseGetAuth);

        Assertions.assertResponseCodeeEquals(responseUserDelete, "400");
        Assertions.assertResponseTextEquals(responseUserDelete, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
        Assertions.assertResponseCodeeEquals(responseUserData, "200");
        Assertions.assertJsonByName(responseUserData, KEY_USERNAME, newUserData.get(KEY_USERNAME.toString()));
    }

    private void createNewUser() {
        newUserData = DataGenerator.getNewUserData();
        userId = apiCoreRequests.makePostRequest(URL_API_USER.toString(), newUserData)
                .jsonPath()
                .get(KEY_ID.toString());
    }
}