import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShortPhraseTest {

    @Test
    public void testShortPhrase() {
        String testingPhrase = "your phrase here please";
        assertTrue(testingPhrase.length() > 15, "Your phrase is not longer than 15");
    }
}