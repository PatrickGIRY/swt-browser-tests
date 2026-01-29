package poc.swt.browser.tests.app.viewmodel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import poc.swt.browser.tests.app.model.HtmlDocument;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class DocumentViewModelTest {

    private DocumentViewModel viewModel;

    @Mock
    private Consumer<LocationUpdated> currentUrlUpdatedConsumer;

    @BeforeEach
    void setUp() {
        viewModel = new DocumentViewModel(currentUrlUpdatedConsumer);
    }

    @Test
    void create_view_model() {
        assertEquals(HtmlDocument.ABOUT_BLANK_URL, viewModel.addressBarText());
        assertEquals(HtmlDocument.ABOUT_BLANK_URL, viewModel.browserUrl());
        assertEquals("", viewModel.browserText());
        assertEquals(new HtmlDocument(), viewModel.document());

        then(currentUrlUpdatedConsumer).shouldHaveNoInteractions();
    }

    @Test
    void change_location() {
        final var newUrl = "http://newUrl";
        viewModel.setAddressBarText(newUrl);
        viewModel.changeLocation();

        assertEquals(newUrl, viewModel.browserUrl());
        assertEquals("", viewModel.browserText());
        assertEquals(new HtmlDocument(newUrl), viewModel.document());

        then(currentUrlUpdatedConsumer).should().accept(new LocationUpdated());
    }

    @Test
    void change_location_with_empty_url() {
        final var newUrl = "";
        viewModel.setAddressBarText(newUrl);
        viewModel.changeLocation();

        assertEquals(HtmlDocument.ABOUT_BLANK_URL, viewModel.browserUrl());
        assertEquals("", viewModel.browserText());
        assertEquals(new HtmlDocument(newUrl), viewModel.document());
        assertEquals(HtmlDocument.ABOUT_BLANK_URL, viewModel.addressBarText());

        then(currentUrlUpdatedConsumer).should().accept(new LocationUpdated());
    }

    @Test
    void set_browser_text() {
        final var newContent = "newContent";

        viewModel.setBrowserText(newContent);

        assertEquals(newContent, viewModel.browserText());
        assertEquals(new HtmlDocument("",  newContent), viewModel.document());

    }

    @ParameterizedTest
    @ValueSource(strings = {""," "})
    void set_empty_browser_url(String url) {

        viewModel.setBrowserUrl(url);

        assertEquals(HtmlDocument.ABOUT_BLANK_URL, viewModel.browserUrl());
        assertEquals(new HtmlDocument(HtmlDocument.ABOUT_BLANK_URL,  ""), viewModel.document());
    }

    @Test
    void set_null_browser_url() {

        viewModel.setBrowserUrl(null);

        assertEquals(HtmlDocument.ABOUT_BLANK_URL, viewModel.browserUrl());
        assertEquals(new HtmlDocument(HtmlDocument.ABOUT_BLANK_URL,  ""), viewModel.document());
    }
}