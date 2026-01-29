package poc.swt.browser.tests.app.viewmodel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import poc.swt.browser.tests.app.model.HtmlDocument;

import static org.junit.jupiter.api.Assertions.*;

class DocumentViewModelTest {

    private DocumentViewModel viewModel;
    private LocationUpdated locationUpdated;

    @BeforeEach
    void setUp() {
        viewModel = new DocumentViewModel(locationUpdated -> this.locationUpdated = locationUpdated);
    }

    @Test
    void create_view_model() {
        assertEquals(HtmlDocument.ABOUT_BLANK_URL, viewModel.addressBarText());
        assertEquals(HtmlDocument.ABOUT_BLANK_URL, viewModel.browserUrl());
        assertEquals("", viewModel.browserText());
        assertNull(locationUpdated);
        assertEquals(new HtmlDocument(), viewModel.document());
    }

    @Test
    void change_location() {
        final var newUrl = "http://newUrl";
        viewModel.setAddressBarText(newUrl);
        viewModel.changeLocation();

        assertEquals(newUrl, viewModel.browserUrl());
        assertEquals("", viewModel.browserText());
        assertEquals(new LocationUpdated(), locationUpdated);
        assertEquals(new HtmlDocument(newUrl), viewModel.document());
    }

    @Test
    void change_location_with_empty_url() {
        final var newUrl = "";
        viewModel.setAddressBarText(newUrl);
        viewModel.changeLocation();

        assertEquals(HtmlDocument.ABOUT_BLANK_URL, viewModel.browserUrl());
        assertEquals("", viewModel.browserText());
        assertEquals(new LocationUpdated(), locationUpdated);
        assertEquals(new HtmlDocument(newUrl), viewModel.document());
        assertEquals(HtmlDocument.ABOUT_BLANK_URL, viewModel.addressBarText());
    }

    @Test
    void set_browser_text() {
        final var newContent = "newContent";

        viewModel.setBrowserText(newContent);

        assertEquals(newContent, viewModel.browserText());
        assertEquals(new HtmlDocument("",  newContent), viewModel.document());

    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"  "})
    void set_null_or_empty_browser_url(String url) {

        viewModel.setBrowserUrl(url);

        assertEquals(HtmlDocument.ABOUT_BLANK_URL, viewModel.browserUrl());
        assertEquals(new HtmlDocument(HtmlDocument.ABOUT_BLANK_URL,  ""), viewModel.document());
    }
}