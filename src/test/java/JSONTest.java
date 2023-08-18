import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class JSONTest {

    @Test
    public void testJSON() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        ArrayList<LinkedHashMap<String, String>> messages = response.get("messages");
        LinkedHashMap<String, String> secondMap = messages.get(1);
        String secondMessage = secondMap.get("message");
        System.out.println(secondMessage);
    }
}