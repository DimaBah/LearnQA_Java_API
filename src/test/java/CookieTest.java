import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CookieTest {

    @Test
    public void testCookie() {
        Response response = RestAssured.get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        String cookie = response.getCookie("HomeWork");
        assertEquals("hw_value", cookie, "Cookie is`n correct ");
    }
}