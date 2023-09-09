package lib;

public enum StringConstants {

    /***** Keys *****/
    KEY_EMAIL("email"),
    KEY_ID("id"),
    KEY_PASSWORD("password"),
    KEY_USERNAME("username"),
    KEY_FIRSTNAME("firstName"),
    KEY_LASTNAME("lastName"),
    KEY_AUTH_COOKIE("auth_sid"),
    KEY_AUTH_TOKEN("x-csrf-token"),

    /***** URLs *****/
    URL_API_USER("https://playground.learnqa.ru/api/user/"),
    URL_API_USER_LOGIN("https://playground.learnqa.ru/api/user/login"),
    URL_API_USER_ID_PATTERN("https://playground.learnqa.ru/api/user/%s");

    private final String text;

    StringConstants(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}