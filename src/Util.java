/**
 * @author Duc Minh Le (s3651764)
 */
public class Util {

    public static String[] splitAndTrimTokens(String s, String regex) {
        String[] tokens = s.split(regex);
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].trim();
        }
        return tokens;
    }

}
