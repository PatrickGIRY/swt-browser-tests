package poc.swt.browser.tests.app.viewmodel;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BrowserSearchViewModelTest {

    private ContentEnrichedBySearchResults contentEnrichedBySearchResults;

    @Test
    void create_browser_search_view_model() {
        final var browserSearchViewModel = new BrowserSearchViewModel("",
                contentEnrichedBySearchResults -> this.contentEnrichedBySearchResults = contentEnrichedBySearchResults);

        assertEquals("",browserSearchViewModel.searchText());
        assertFalse(browserSearchViewModel.caseSensitive());
        assertFalse(browserSearchViewModel.wholeWord());
        assertFalse(browserSearchViewModel.nextOccurrenceEnabled());
        assertFalse(browserSearchViewModel.previousOccurrenceEnabled());
        assertNull(contentEnrichedBySearchResults);
    }

    @Test
    void search_one_occurrence_in_text_in_html_document_text_nodes() {
        final var browserSearchViewModel = new BrowserSearchViewModel(Jsoup.parseBodyFragment("""
                <p>This is a test text</p>
                """).html(),
                contentEnrichedBySearchResults -> this.contentEnrichedBySearchResults = contentEnrichedBySearchResults);

        browserSearchViewModel.setSearchText("test");
        browserSearchViewModel.searchOccurrences();

        assertEquals(Jsoup.parseBodyFragment("""
                <p>This is a <span style='background-color: yellow' id='match-1'>test</span> text</p>
                """).html(), contentEnrichedBySearchResults.enrichedContent());

        assertFalse(browserSearchViewModel.nextOccurrenceEnabled());
        assertFalse(browserSearchViewModel.previousOccurrenceEnabled());
    }
    @Test
    void search_several_occurrences_in_text_in_html_document_text_nodes() {
        final var browserSearchViewModel = new BrowserSearchViewModel(Jsoup.parseBodyFragment("""
                <p>This is a test text.</p>
                <p>This is an other test text.</p>
                """).html(),
                contentEnrichedBySearchResults -> this.contentEnrichedBySearchResults = contentEnrichedBySearchResults);

        browserSearchViewModel.setSearchText("test");
        browserSearchViewModel.searchOccurrences();

        assertEquals(Jsoup.parseBodyFragment("""
                <p>This is a <span style='background-color: yellow' id='match-1'>test</span> text.</p>
                <p>This is an other <span style='background-color: yellow' id='match-2'>test</span> text.</p>
                """).html(), contentEnrichedBySearchResults.enrichedContent());

        assertTrue(browserSearchViewModel.nextOccurrenceEnabled());
        assertFalse(browserSearchViewModel.previousOccurrenceEnabled());
    }
}