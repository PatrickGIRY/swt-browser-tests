package poc.swt.browser.tests.app.viewmodel;

import poc.swt.browser.tests.app.eventbus.EventBus;
import poc.swt.browser.tests.app.model.HtmlDocument;

public class DocumentViewModel {
    private HtmlDocument document;

    private final EventBus<Event> eventBus;

    public DocumentViewModel(EventBus<Event> eventBus) {
        this.eventBus = eventBus;
        this.document = new HtmlDocument();
    }

    public void setCurrentUrl(String newUrl) {
        if (newUrl != null && !newUrl.isEmpty()) {
            final var oldUrl = document.url();
            this.document = new HtmlDocument(newUrl);
            eventBus.publish(new CurrentUrlUpdated(oldUrl, newUrl));
        }
    }

    public String currentUrl() {
        return document.url();
    }

    public void setNewContent(String url, String content) {
        document = new HtmlDocument(url, content);
    }
}
