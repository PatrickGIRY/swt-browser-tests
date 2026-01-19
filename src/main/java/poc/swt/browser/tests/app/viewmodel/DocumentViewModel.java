package poc.swt.browser.tests.app.viewmodel;

import poc.swt.browser.tests.app.model.HtmlDocument;

public class DocumentViewModel {
    private HtmlDocument document;
    private String currentUrl;

    public DocumentViewModel() {
        this.document = new HtmlDocument();
        this.currentUrl = "about:blank";
    }

    public void loadUrl(String url) {
        this.currentUrl = url;
        this.document = new HtmlDocument(url, "");
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public HtmlDocument getDocument() {
        return document;
    }
}
