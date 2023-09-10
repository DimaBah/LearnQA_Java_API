package tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static lib.StringConstants.*;

public class UserEditTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testEditJustCreatedUser() {
        //Generate user
        Map<String, String> userData = DataGenerator.getNewUserData();
        JsonPath responseCreateAuth = apiCoreRequests.makePostRequest(URL_API_USER.toString(), userData)
                .jsonPath();

        String userId = responseCreateAuth.get(KEY_ID.toString());

        //Login
        Map<String, String> authData = new HashMap<>();
        authData.put(KEY_EMAIL.toString(), userData.get(KEY_EMAIL.toString()));
        authData.put(KEY_PASSWORD.toString(), userData.get(KEY_PASSWORD.toString()));

        Response responseGetAuth = apiCoreRequests.makePostRequest(URL_API_USER_LOGIN.toString(), authData);

        String headerResponseGetAuth = this.getHeader(responseGetAuth, KEY_AUTH_TOKEN.toString());
        String cookieResponseGetAuth = this.getCookie(responseGetAuth, KEY_AUTH_COOKIE.toString());

        //Edit
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put(KEY_FIRSTNAME.toString(), newName);
        String urlApiUserId = String.format(URL_API_USER_ID_PATTERN.toString(), userId);

        apiCoreRequests.makePutRequest(urlApiUserId, headerResponseGetAuth, cookieResponseGetAuth, editData);

        //Get new data
        Response responseUserData = apiCoreRequests.makeGetRequest(urlApiUserId,
                headerResponseGetAuth,
                cookieResponseGetAuth);

        Assertions.assertJsonByName(responseUserData, KEY_FIRSTNAME, newName);
    }
}