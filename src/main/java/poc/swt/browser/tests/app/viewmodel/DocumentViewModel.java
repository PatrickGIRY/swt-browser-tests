package poc.swt.browser.tests.app.viewmodel;

import poc.swt.browser.tests.app.model.HtmlDocument;

public class DocumentViewModel {
    private HtmlDocument document;

    public DocumentViewModel() {
        this.document = new HtmlDocument();
    }

    public void loadUrl(String url) {
        this.document = new HtmlDocument(url, "");
    }

    public String currentUrl() {
        return document.url();
    }

}
