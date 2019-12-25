package allwhite.site.guides;

public class WebhookAuthenticationException extends RuntimeException {
    public WebhookAuthenticationException(String expected, String actual) {
        super(String.format("Could not verify signature: '%s', expected '%s'", actual, expected));
    }
}
