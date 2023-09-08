package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_LASTNAME = "lastName";
    private static final String URL_USER = "https://playground.learnqa.ru/api/user/";

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";
        Map<String, String> userData = new HashMap<>();
        userData.put(KEY_EMAIL, email);
        userData.put(KEY_PASSWORD, "123");
        userData.put(KEY_USERNAME, "learnqa");
        userData.put(KEY_FIRSTNAME, "learnqa");
        userData.put(KEY_LASTNAME, "learnqa");

        Response createAuthResponse = apiCoreRequests.makePostRequest(URL_USER, userData);

        Assertions.assertResponseCodeeEquals(createAuthResponse, "400");
        Assertions.assertResponseTextEquals(createAuthResponse, "Users with email '" + email + "' already exists");
    }

    @Test
    public void testCreateUserSuccessful() {
        String email = DataGenerator.getRandomEmail();
        Map<String, String> userData = new HashMap<>();
        userData.put(KEY_EMAIL, email);
        userData.put(KEY_PASSWORD, "123");
        userData.put(KEY_USERNAME, "learnqa");
        userData.put(KEY_FIRSTNAME, "learnqa");
        userData.put(KEY_LASTNAME, "learnqa");

        Response createAuthResponse = apiCoreRequests.makePostRequest(URL_USER, userData);

        Assertions.assertResponseCodeeEquals(createAuthResponse, "200");
        Assertions.assertJsonHasField(createAuthResponse, "id");
    }

    @Test
    public void testCreateUserWithIncorrectEmail() {
        String email = DataGenerator.getRandomEmail()
                .replaceFirst("@", "");
        Map<String, String> userData = new HashMap<>();
        userData.put(KEY_EMAIL, email);
        userData.put(KEY_PASSWORD, "123");
        userData.put(KEY_USERNAME, "learnqa");
        userData.put(KEY_FIRSTNAME, "learnqa");
        userData.put(KEY_LASTNAME, "learnqa");

        Response createAuthResponse = apiCoreRequests.makePostRequest(URL_USER, userData);

        Assertions.assertResponseCodeeEquals(createAuthResponse, "400");
        Assertions.assertJsonHasNotField(createAuthResponse, "id");
        Assertions.assertResponseTextEquals(createAuthResponse, "Invalid email format");
    }

    @Test
    public void testCreateUserWithShortUsername() {
        String email = DataGenerator.getRandomEmail();
        String username = DataGenerator.getRandomString(1);
        Map<String, String> userData = new HashMap<>();
        userData.put(KEY_EMAIL, email);
        userData.put(KEY_PASSWORD, "123");
        userData.put(KEY_USERNAME, username);
        userData.put(KEY_FIRSTNAME, "learnqa");
        userData.put(KEY_LASTNAME, "learnqa");

        Response createAuthResponse = apiCoreRequests.makePostRequest(URL_USER, userData);

        Assertions.assertResponseCodeeEquals(createAuthResponse, "400");
        Assertions.assertJsonHasNotField(createAuthResponse, "id");
        Assertions.assertResponseTextEquals(createAuthResponse, "The value of 'username' field is too short");
    }

    @Test
    public void testCreateUserWithLongUsername() {
        String email = DataGenerator.getRandomEmail();
        String username = DataGenerator.getRandomString(251);
        Map<String, String> userData = new HashMap<>();
        userData.put(KEY_EMAIL, email);
        userData.put(KEY_PASSWORD, "123");
        userData.put(KEY_USERNAME, username);
        userData.put(KEY_FIRSTNAME, "learnqa");
        userData.put(KEY_LASTNAME, "learnqa");

        Response createAuthResponse = apiCoreRequests.makePostRequest(URL_USER, userData);

        Assertions.assertResponseCodeeEquals(createAuthResponse, "400");
        Assertions.assertJsonHasNotField(createAuthResponse, "id");
        Assertions.assertResponseTextEquals(createAuthResponse, "The value of 'username' field is too long");
    }

    @ParameterizedTest
    @ValueSource(strings = {KEY_EMAIL, KEY_PASSWORD, KEY_USERNAME, KEY_FIRSTNAME, KEY_LASTNAME})
    public void testCreateUserWithIncompleteData(String noKeyPresented) {
        String email = DataGenerator.getRandomEmail();
        String username = DataGenerator.getRandomString(10);
        Map<String, String> userData = new HashMap<>();
        userData.put(KEY_EMAIL, email);
        userData.put(KEY_PASSWORD, "123");
        userData.put(KEY_USERNAME, username);
        userData.put(KEY_FIRSTNAME, "learnqa");
        userData.put(KEY_LASTNAME, "learnqa");
        userData.remove(noKeyPresented);

        Response createAuthResponse = apiCoreRequests.makePostRequest(URL_USER, userData);

        Assertions.assertResponseCodeeEquals(createAuthResponse, "400");
        Assertions.assertJsonHasNotField(createAuthResponse, "id");
        Assertions.assertResponseTextEquals(createAuthResponse, "The following required params are missed: " + noKeyPresented);
    }
}