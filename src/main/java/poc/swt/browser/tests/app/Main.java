package poc.swt.browser.tests.app;


import org.eclipse.swt.widgets.Display;

public class Main {
    public static void main(String[] args) {
        final var display = Display.getDefault();
        final var searchView = new SearchView(display);
        searchView.open();
        display.dispose();
    }
}