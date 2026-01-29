package poc.swt.browser.tests.app.viewmodel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class DocumentViewModelTest {

    private DocumentViewModel viewModel;

    @Mock
    private Consumer<LocationUpdated> currentUrlUpdatedConsumer;

    @Mock
    private UnaryOperator<String> cacheDocument;

    @BeforeEach
    void setUp() {
        viewModel = new DocumentViewModel(cacheDocument, currentUrlUpdatedConsumer);
    }

    @Test
    void create_view_model() {
        assertEquals(DocumentViewModel.ABOUT_BLANK_URL, viewModel.addressBarText());
        assertEquals(DocumentViewModel.ABOUT_BLANK_URL, viewModel.browserUrl());
        assertEquals("", viewModel.browserText());

        then(currentUrlUpdatedConsumer).shouldHaveNoInteractions();
    }

    @Test
    void change_location() {
        final var newUrl = "http://newUrl";
        final var internalUrl = "file://internalUrl";

        given(cacheDocument.apply(newUrl)).willReturn(internalUrl);
        viewModel.setAddressBarText(newUrl);
        viewModel.changeLocation();

        assertEquals(internalUrl, viewModel.browserUrl());
        assertEquals("", viewModel.browserText());

        then(currentUrlUpdatedConsumer).should().accept(new LocationUpdated());
    }

    @Test
    void change_location_with_empty_url() {
        final var newUrl = "";
        viewModel.setAddressBarText(newUrl);
        viewModel.changeLocation();

        assertEquals(DocumentViewModel.ABOUT_BLANK_URL, viewModel.browserUrl());
        assertEquals("", viewModel.browserText());
        assertEquals(DocumentViewModel.ABOUT_BLANK_URL, viewModel.addressBarText());

        then(currentUrlUpdatedConsumer).should().accept(new LocationUpdated());
    }

    @Test
    void set_browser_text() {
        final var newContent = "newContent";

        viewModel.setBrowserText(newContent);

        assertEquals(newContent, viewModel.browserText());

    }

    @ParameterizedTest
    @ValueSource(strings = {""," "})
    void set_empty_browser_url(String url) {

        viewModel.setBrowserUrl(url);

        assertEquals(DocumentViewModel.ABOUT_BLANK_URL, viewModel.browserUrl());
    }

    @Test
    void set_null_browser_url() {

        viewModel.setBrowserUrl(null);

        assertEquals(DocumentViewModel.ABOUT_BLANK_URL, viewModel.browserUrl());
    }
}