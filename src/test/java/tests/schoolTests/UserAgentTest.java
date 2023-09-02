package tests.schoolTests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserAgentTest {
    private static final String KEY_USER_AGENT = "user-agent";
    private static final String KEY_PLATFORM = "platform";
    private static final String KEY_BROWSER = "browser";
    private static final String KEY_DEVICE = "device";

    private static List<Map<String, String>> getUserAgents() {
        Document document;
        try {
            document = Jsoup.connect("https://gist.github.com/KotovVitaliy/138894aa5b6fa442163561b5db6e2e26")
                    .get();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get file.");
        }
        List<String> userAgents = document.body()
                .selectXpath("//*[contains(text(), 'Mozilla/5.0')]")
                .stream()
                .map(Element::text)
                .collect(Collectors.toList());
        List<String[]> expectedValues = document.body()
                .selectXpath("//*[contains(text(), 'platform')]")
                .stream()
                .map(element -> element.text().replaceAll("'", "").split(", "))
                .collect(Collectors.toList());
        List<Map<String, String>> userAgentsData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, String> data = new HashMap<>();
            data.put(KEY_USER_AGENT, userAgents.get(i));
            for (String keyAndValue : expectedValues.get(i)) {
                String key = keyAndValue.split(": ")[0];
                String value = keyAndValue.split(": ")[1];
                data.put(key, value);
            }
            userAgentsData.add(data);
        }
        return userAgentsData;
    }

    @ParameterizedTest
    @MethodSource("getUserAgents")
    public void testUserAgent(Map<String, String> userAgentsDataExpected) {
        JsonPath response = RestAssured.given()
                .header(KEY_USER_AGENT, userAgentsDataExpected.get(KEY_USER_AGENT))
                .when()
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();
        System.out.println("Checking User Agent: " + response.get("user_agent"));
        assertEquals(userAgentsDataExpected.get(KEY_PLATFORM), response.get(KEY_PLATFORM), "Platforms are not equal");
        assertEquals(userAgentsDataExpected.get(KEY_BROWSER), response.get(KEY_BROWSER), "Browsers are not equal");
        assertEquals(userAgentsDataExpected.get(KEY_DEVICE), response.get(KEY_DEVICE), "Devices are not equal");
    }
}