package lib;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataGenerator {

    public static String getRandomEmail() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return "learnqa" + timeStamp + "@example.com";
    }

    public static String getRandomString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }
}