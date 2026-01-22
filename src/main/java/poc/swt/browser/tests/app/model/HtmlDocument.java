package poc.swt.browser.tests.app.model;

import java.net.MalformedURLException;
import java.net.URL;

import static java.util.Objects.isNull;

public record HtmlDocument(String url, String content) {

    public static final String ABOUT_BLANK_URL = "about:blank";

    public HtmlDocument() {
        this(ABOUT_BLANK_URL, "");
    }

    public HtmlDocument(String url) {
        this(url, "");
    }

    public HtmlDocument(String url, String content) {
        try {
            this.url = isAboutBlankUrl(url) || isNullOREmpty(url) ? ABOUT_BLANK_URL : new URL(url).toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.content = content != null ? content : "";
    }

    private static boolean isNullOREmpty(String url) {
        return isNull(url) || url.trim().isEmpty();
    }

    private static boolean isAboutBlankUrl(String url) {
        return ABOUT_BLANK_URL.equals(url);
    }
}
