package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Map;

import static lib.StringConstants.*;

public class UserRegisterTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    private static ArrayList<String> getKeysUserData() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add(KEY_EMAIL.toString());
        keys.add(KEY_PASSWORD.toString());
        keys.add(KEY_USERNAME.toString());
        keys.add(KEY_FIRSTNAME.toString());
        keys.add(KEY_LASTNAME.toString());
        return keys;
    }

    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";
        Map<String, String> userData = DataGenerator.getNewUserData();
        userData.replace(KEY_EMAIL.toString(), email);

        Response createAuthResponse = apiCoreRequests.makePostRequest(URL_API_USER.toString(), userData);

        Assertions.assertResponseCodeeEquals(createAuthResponse, "400");
        Assertions.assertResponseTextEquals(createAuthResponse, "Users with email '" + email + "' already exists");
    }

    @Test
    public void testCreateUserSuccessful() {
        Map<String, String> userData = DataGenerator.getNewUserData();

        Response createAuthResponse = apiCoreRequests.makePostRequest(URL_API_USER.toString(), userData);

        Assertions.assertResponseCodeeEquals(createAuthResponse, "200");
        Assertions.assertJsonHasField(createAuthResponse, "id");
    }

    @Test
    public void testCreateUserWithIncorrectEmail() {
        Map<String, String> userData = DataGenerator.getNewUserData();
        userData.replace(KEY_EMAIL.toString(), userData.get(KEY_EMAIL.toString()).replaceFirst("@", ""));

        Response createAuthResponse = apiCoreRequests.makePostRequest(URL_API_USER.toString(), userData);

        Assertions.assertResponseCodeeEquals(createAuthResponse, "400");
        Assertions.assertJsonHasNotField(createAuthResponse, "id");
        Assertions.assertResponseTextEquals(createAuthResponse, "Invalid email format");
    }

    @Test
    public void testCreateUserWithShortUsername() {
        Map<String, String> userData = DataGenerator.getNewUserData();
        userData.replace(KEY_USERNAME.toString(), DataGenerator.getRandomString(1));

        Response createAuthResponse = apiCoreRequests.makePostRequest(URL_API_USER.toString(), userData);

        Assertions.assertResponseCodeeEquals(createAuthResponse, "400");
        Assertions.assertJsonHasNotField(createAuthResponse, "id");
        Assertions.assertResponseTextEquals(createAuthResponse, "The value of 'username' field is too short");
    }

    @Test
    public void testCreateUserWithLongUsername() {
        Map<String, String> userData = DataGenerator.getNewUserData();
        userData.replace(KEY_USERNAME.toString(), DataGenerator.getRandomString(251));

        Response createAuthResponse = apiCoreRequests.makePostRequest(URL_API_USER.toString(), userData);

        Assertions.assertResponseCodeeEquals(createAuthResponse, "400");
        Assertions.assertJsonHasNotField(createAuthResponse, "id");
        Assertions.assertResponseTextEquals(createAuthResponse, "The value of 'username' field is too long");
    }

    @ParameterizedTest(name = "Without field: {0}")
    @MethodSource("getKeysUserData")
    public void testCreateUserWithIncompleteData(String noKeyPresented) {
        Map<String, String> userData = DataGenerator.getNewUserData();
        userData.remove(noKeyPresented);

        Response createAuthResponse = apiCoreRequests.makePostRequest(URL_API_USER.toString(), userData);

        Assertions.assertResponseCodeeEquals(createAuthResponse, "400");
        Assertions.assertJsonHasNotField(createAuthResponse, "id");
        Assertions.assertResponseTextEquals(createAuthResponse, "The following required params are missed: " + noKeyPresented);
    }
}