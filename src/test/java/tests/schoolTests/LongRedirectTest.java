package tests.schoolTests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class LongRedirectTest {

    @Test
    public void testLongRedirect() {
        int countRedirect = 0;
        int statusResponse = 0;
        String url = "https://playground.learnqa.ru/api/long_redirect";
        while (statusResponse != 200) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(url)
                    .andReturn();
            String headerLocation = response.getHeader("Location");
            statusResponse = response.statusCode();
            url = headerLocation;
            if (statusResponse >= 300 && statusResponse <= 399) {
                countRedirect++;
            }
            System.out.printf("\nStatus %s.\nThe Location is \"%s\".", statusResponse, headerLocation);
        }
        System.out.printf("\nRedirects count is %s.\n", countRedirect);
    }
}