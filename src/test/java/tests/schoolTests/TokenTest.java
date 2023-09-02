package tests.schoolTests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class TokenTest {

    @Test
    public void testToken() throws InterruptedException {
        String URL_JOB = "https://playground.learnqa.ru/ajax/api/longtime_job";
        JsonPath initialResponse = RestAssured.get(URL_JOB)
                .jsonPath();
        String tokenInitialResponse = initialResponse.get("token");
        int secondsInitialResponse = initialResponse.get("seconds");
        JsonPath secondResponse = RestAssured.given()
                .queryParam("token", tokenInitialResponse)
                .get(URL_JOB)
                .jsonPath();
        String statusSecondResponse = secondResponse.get("status");
        System.out.printf("\nStatus: \"%s\".", statusSecondResponse);
        boolean isStatusSecondResponseOK = statusSecondResponse.equals("Job is NOT ready");
        System.out.printf("\nStatus of second response is OK: %s.\n", isStatusSecondResponseOK);
        Thread.sleep(secondsInitialResponse * 1000L);
        JsonPath finishResponse = RestAssured.given()
                .queryParam("token", tokenInitialResponse)
                .get(URL_JOB)
                .jsonPath();
        String statusFinishResponse = finishResponse.get("status");
        String resultFinishResponse = finishResponse.get("result");
        boolean isStatusFinishResponseOK = statusFinishResponse.equals("Job is ready");
        System.out.printf("\nStatus: \"%s\". Result is \"%s\"", statusFinishResponse, resultFinishResponse);
        System.out.printf("\nStatus of final response is OK: %s.\n", isStatusFinishResponseOK);
    }
}