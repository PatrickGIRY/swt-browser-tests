package poc.swt.browser.tests.app.viewmodel;

import poc.swt.browser.tests.app.model.HtmlDocument;

import java.util.function.Consumer;

public class DocumentViewModel {

    private String addressBarText;

    private String browserUrl;

    private String browserText;

    private final Consumer<LocationUpdated> currentUrlUpdatedConsumer;

    private HtmlDocument document;

    public DocumentViewModel(Consumer<LocationUpdated> currentUrlUpdatedConsumer) {
        this.currentUrlUpdatedConsumer = currentUrlUpdatedConsumer;
        this.document = new HtmlDocument();
        initFromDocument();
    }

    private void initFromDocument() {
        addressBarText = document.url();
        browserUrl =  document.url();
        browserText = document.content();
    }

    public String addressBarText() {
        return addressBarText;
    }

    public void setAddressBarText(String addressBarText) {
        this.addressBarText = addressBarText;
    }

    public String browserUrl() {
        return browserUrl;
    }

    public void setBrowserUrl(String browserUrl) {
        this.browserUrl = browserUrl;
        updateDocument();
    }

    public String browserText() {
        return browserText;
    }

    public void setBrowserText(String browserText) {
        this.browserText = browserText;
        updateDocument();
    }

    public HtmlDocument document() {
        return document;
    }

    public void changeLocation() {
        if (addressBarText != null && !addressBarText.isEmpty()) {
           setBrowserUrl(addressBarText);
           currentUrlUpdatedConsumer.accept(new LocationUpdated(addressBarText));
        } else {
            setBrowserUrl("");
            currentUrlUpdatedConsumer.accept(new LocationUpdated(browserUrl));
        }
    }

    private void updateDocument() {
        document = new HtmlDocument(browserUrl, browserText);
        initFromDocument();
    }
}
