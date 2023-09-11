package tests;

import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static lib.StringConstants.*;

public class UserEditNegativeTest extends BaseTestCase {
    private String userId;
    private Map<String, String> newUserData;

    @BeforeEach
    public void createNewUser() throws InterruptedException {
        //Generate new user
        newUserData = DataGenerator.getNewUserData();
        userId = apiCoreRequests.makePostRequest(URL_API_USER.toString(), newUserData)
                .jsonPath()
                .get(KEY_ID.toString());
        Thread.sleep(1);
    }

    @Test
    public void testEditJustCreatedUserWithoutAuthorization() {

        //Edit
        Map<String, String> editData = new HashMap<>();
        editData.put(KEY_FIRSTNAME.toString(), "Changed Name");
        String urlApiUserId = String.format(URL_API_USER_ID_PATTERN.toString(), userId);

        apiCoreRequests.makePutRequest(urlApiUserId, editData);

        //Check data
        Response responseUserData = apiCoreRequests.makeGetRequest(urlApiUserId);

        Assertions.assertJsonByName(responseUserData, KEY_USERNAME, newUserData.get(KEY_USERNAME.toString()));
        Assertions.assertJsonHasNotField(responseUserData, KEY_EMAIL);
        Assertions.assertJsonHasNotField(responseUserData, KEY_FIRSTNAME);
        Assertions.assertJsonHasNotField(responseUserData, KEY_LASTNAME);
        Assertions.assertJsonHasNotField(responseUserData, KEY_PASSWORD);
        Assertions.assertJsonHasNotField(responseUserData, KEY_ID);
    }

    @Test
    public void testEditJustCreatedUserWithAnotherUserAuthorization() {
        //Login as another user
        Map<String, String> authData = new HashMap<>();
        authData.put(KEY_EMAIL.toString(), "vinkotov@example.com");
        authData.put(KEY_PASSWORD.toString(), "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest(URL_API_USER_LOGIN.toString(), authData);

        String headerResponseGetAuth = getHeader(responseGetAuth, KEY_AUTH_TOKEN.toString());
        String cookieResponseGetAuth = getCookie(responseGetAuth, KEY_AUTH_COOKIE.toString());

        //Edit
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put(KEY_FIRSTNAME.toString(), newName);
        String urlApiUserId = String.format(URL_API_USER_ID_PATTERN.toString(), userId);

        apiCoreRequests.makePutRequest(urlApiUserId, headerResponseGetAuth, cookieResponseGetAuth, editData);

        //Get new data
        Response responseUserData = apiCoreRequests.makeGetRequest(urlApiUserId, headerResponseGetAuth, cookieResponseGetAuth);

        Assertions.assertJsonByName(responseUserData, KEY_USERNAME, newUserData.get(KEY_USERNAME.toString()));
        Assertions.assertJsonHasNotField(responseUserData, KEY_EMAIL);
        Assertions.assertJsonHasNotField(responseUserData, KEY_FIRSTNAME);
        Assertions.assertJsonHasNotField(responseUserData, KEY_LASTNAME);
        Assertions.assertJsonHasNotField(responseUserData, KEY_PASSWORD);
        Assertions.assertJsonHasNotField(responseUserData, KEY_ID);
    }

    @Test
    public void testEditJustCreatedUserWithBadEmail() {
        //Login
        Map<String, String> authData = new HashMap<>();
        authData.put(KEY_EMAIL.toString(), newUserData.get(KEY_EMAIL.toString()));
        authData.put(KEY_PASSWORD.toString(), newUserData.get(KEY_PASSWORD.toString()));

        Response responseGetAuth = apiCoreRequests.makePostRequest(URL_API_USER_LOGIN.toString(), authData);

        String headerResponseGetAuth = getHeader(responseGetAuth, KEY_AUTH_TOKEN.toString());
        String cookieResponseGetAuth = getCookie(responseGetAuth, KEY_AUTH_COOKIE.toString());

        //Edit
        String newEmail = DataGenerator.getRandomEmail()
                .replaceFirst("@", "");
        Map<String, String> editData = new HashMap<>();
        editData.put(KEY_EMAIL.toString(), newEmail);
        String urlApiUserId = String.format(URL_API_USER_ID_PATTERN.toString(), userId);

        apiCoreRequests.makePutRequest(urlApiUserId, headerResponseGetAuth, cookieResponseGetAuth, editData);

        //Get new data
        Response responseUserData = apiCoreRequests.makeGetRequest(urlApiUserId, headerResponseGetAuth, cookieResponseGetAuth);

        Assertions.assertJsonByName(responseUserData, KEY_USERNAME, newUserData.get(KEY_USERNAME.toString()));
        Assertions.assertJsonByName(responseUserData, KEY_EMAIL, newUserData.get(KEY_EMAIL.toString()));
        Assertions.assertJsonByName(responseUserData, KEY_FIRSTNAME, newUserData.get(KEY_FIRSTNAME.toString()));
        Assertions.assertJsonByName(responseUserData, KEY_LASTNAME, newUserData.get(KEY_LASTNAME.toString()));
        Assertions.assertJsonByName(responseUserData, KEY_ID, userId);
    }

    @Test
    public void testEditJustCreatedUserWithShortFirstname() {
        //Login
        Map<String, String> authData = new HashMap<>();
        authData.put(KEY_EMAIL.toString(), newUserData.get(KEY_EMAIL.toString()));
        authData.put(KEY_PASSWORD.toString(), newUserData.get(KEY_PASSWORD.toString()));

        Response responseGetAuth = apiCoreRequests.makePostRequest(URL_API_USER_LOGIN.toString(), authData);

        String headerResponseGetAuth = getHeader(responseGetAuth, KEY_AUTH_TOKEN.toString());
        String cookieResponseGetAuth = getCookie(responseGetAuth, KEY_AUTH_COOKIE.toString());

        //Edit
        String newFirstname = DataGenerator.getRandomString(1);
        Map<String, String> editData = new HashMap<>();
        editData.put(KEY_FIRSTNAME.toString(), newFirstname);
        String urlApiUserId = String.format(URL_API_USER_ID_PATTERN.toString(), userId);

        apiCoreRequests.makePutRequest(urlApiUserId, headerResponseGetAuth, cookieResponseGetAuth, editData);

        //Get new data
        Response responseUserData = apiCoreRequests.makeGetRequest(urlApiUserId, headerResponseGetAuth, cookieResponseGetAuth);

        Assertions.assertJsonByName(responseUserData, KEY_USERNAME, newUserData.get(KEY_USERNAME.toString()));
        Assertions.assertJsonByName(responseUserData, KEY_EMAIL, newUserData.get(KEY_EMAIL.toString()));
        Assertions.assertJsonByName(responseUserData, KEY_FIRSTNAME, newUserData.get(KEY_FIRSTNAME.toString()));
        Assertions.assertJsonByName(responseUserData, KEY_LASTNAME, newUserData.get(KEY_LASTNAME.toString()));
        Assertions.assertJsonByName(responseUserData, KEY_ID, userId);
    }
}