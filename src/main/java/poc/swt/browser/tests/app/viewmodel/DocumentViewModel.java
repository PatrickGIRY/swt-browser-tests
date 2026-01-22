package poc.swt.browser.tests.app.viewmodel;

import poc.swt.browser.tests.app.model.HtmlDocument;

import java.util.function.Consumer;

public class DocumentViewModel {
    private final Consumer<CurrentUrlUpdated> currentUrlUpdatedConsumer;
    private HtmlDocument document;

    public DocumentViewModel(Consumer<CurrentUrlUpdated> currentUrlUpdatedConsumer) {
        this.currentUrlUpdatedConsumer = currentUrlUpdatedConsumer;

        this.document = new HtmlDocument();
    }

    public void setCurrentUrl(String newUrl) {
        if (newUrl != null && !newUrl.isEmpty()) {
            final var oldUrl = document.url();
            this.document = new HtmlDocument(newUrl);
           currentUrlUpdatedConsumer.accept(new CurrentUrlUpdated(oldUrl, newUrl));
        }
    }

    public String currentUrl() {
        return document.url();
    }

    public void setNewContent(String url, String content) {
        document = new HtmlDocument(url, content);
    }
}
