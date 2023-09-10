package lib;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static lib.StringConstants.*;

public class DataGenerator {

    public static String getRandomEmail() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return "learnqa" + timeStamp + "@example.com";
    }

    public static String getRandomString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static Map<String, String> getNewUserData() {
        Map<String, String> userData = new HashMap<>();
        userData.put(KEY_EMAIL.toString(), getRandomEmail());
        userData.put(KEY_PASSWORD.toString(), "123");
        userData.put(KEY_USERNAME.toString(), getRandomString(10));
        userData.put(KEY_FIRSTNAME.toString(), "someText");
        userData.put(KEY_LASTNAME.toString(), "someText");
        return userData;
    }

    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues) {
        Map<String, String> defaultValues = getNewUserData();
        Map<String, String> userData = new HashMap<>();
        String[] keys = {KEY_EMAIL.toString(), KEY_PASSWORD.toString(), KEY_USERNAME.toString(), KEY_FIRSTNAME.toString(), KEY_LASTNAME.toString()};
        for (String key : keys) {
            if (nonDefaultValues.containsKey(key)) {
                userData.put(key, nonDefaultValues.get(key));
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }
}