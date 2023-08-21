import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class PasswordTest {

    @Test
    public void testPassword() throws IOException {
        Document document = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_the_most_common_passwords")
                .get();
        Elements elements = document.body()
                .selectXpath("//caption[contains(text(),'Top 25 most common passwords by year according to SplashData')]/..//td[@align='left']");
        Set<String> passwords = elements.stream()
                .map(Element::text)
                .collect(Collectors.toSet());
        for (String password : passwords) {
            Response firstResponse = RestAssured.given()
                    .queryParam("login", "super_admin")
                    .queryParam("password", password)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();
            String authCookie = firstResponse.getCookie("auth_cookie");
            Response secondResponse = RestAssured.given()
                    .queryParam("login", "super_admin")
                    .queryParam("password", password)
                    .cookie("auth_cookie", authCookie)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();
            String message = secondResponse.asString();
            if (!message.equals("You are NOT authorized")) {
                System.out.println(message);
                System.out.println(password);
                break;
            }
        }
    }
}