package poc.swt.browser.tests.app.viewmodel;

public record CurrentUrlUpdated(String oldUrl, String newUrl) implements Event {
}
