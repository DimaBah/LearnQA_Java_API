package tests.schoolTests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeaderTest {

    @Test
    public void testHeader() {
        Response response = RestAssured.get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        String header = response.getHeader("x-secret-homework-header");
        assertEquals("Some secret value", header, "Header isn`t correct");
    }
}