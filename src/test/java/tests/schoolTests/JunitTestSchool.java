package tests.schoolTests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JunitTestSchool {

    @ParameterizedTest
    @ValueSource(strings = {"", "Username", "John"})
    public void testHelloNoName(String name) {
        Map<String, String> queryParams = new HashMap<>();
        if (!name.isEmpty()) {
            queryParams.put("name", name);
        }
        JsonPath response = RestAssured.given()
                .queryParams(queryParams)
                .when()
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer = response.getString("answer");
        String expectedName = !name.isEmpty()
                ? name
                : "someone";
        assertEquals("Hello, " + expectedName, answer, "Wrong data");
    }

//    @Test
//    public void testHelloWithName(){
//        String name = "Username";
//        JsonPath response = RestAssured.given()
//                .queryParam("name", name)
//                . when()
//                .get("https://playground.learnqa.ru/api/hello")
//                .jsonPath();
//        String answer = response.getString("answer");
//        assertEquals("Hello, " + name, answer, "Wrong data");
//    }
}