package poc.swt.browser.tests.app.model;

import java.net.MalformedURLException;
import java.net.URL;

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
            this.url = ABOUT_BLANK_URL.equals(url) ? ABOUT_BLANK_URL : new URL(url).toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.content = content != null ? content : "";
    }
}
