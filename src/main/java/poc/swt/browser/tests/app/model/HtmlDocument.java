package poc.swt.browser.tests.app.model;

import java.net.MalformedURLException;
import java.net.URL;

public record HtmlDocument(String url, String content) {

    public HtmlDocument() {
        this("about:blank", "");
    }

    public HtmlDocument(String url) {
        this(url, "");
    }

    public HtmlDocument(String url, String content) {
        try {
            this.url = new URL(url).toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.content = content != null ? content : "";
    }
}
