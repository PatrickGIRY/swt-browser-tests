package poc.swt.browser.tests.app.viewmodel;

import java.util.function.Consumer;

import static java.util.Objects.isNull;

public class DocumentViewModel {

    public static final String ABOUT_BLANK_URL = "about:blank";

    private String addressBarText;

    private String browserUrl;

    private String browserText;

    private final Consumer<LocationUpdated> currentUrlUpdatedConsumer;

    public DocumentViewModel(Consumer<LocationUpdated> currentUrlUpdatedConsumer) {
        this.currentUrlUpdatedConsumer = currentUrlUpdatedConsumer;
        addressBarText = ABOUT_BLANK_URL;
        browserUrl = ABOUT_BLANK_URL;
        browserText = "";
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
        if (isNullOREmpty(browserUrl)) {
            this.browserUrl = ABOUT_BLANK_URL;
        } else {
            this.browserUrl = browserUrl;
        }
    }

    private static boolean isNullOREmpty(String url) {
        return isNull(url) || url.trim().isEmpty();
    }

    public String browserText() {
        return browserText;
    }

    public void setBrowserText(String browserText) {
        this.browserText = browserText;
    }

    public void changeLocation() {
        if (addressBarText != null && !addressBarText.isEmpty()) {
            setBrowserUrl(addressBarText);
            currentUrlUpdatedConsumer.accept(new LocationUpdated());
        } else {
            setBrowserUrl(ABOUT_BLANK_URL);
            setAddressBarText(ABOUT_BLANK_URL);
            currentUrlUpdatedConsumer.accept(new LocationUpdated());
        }
    }

}
