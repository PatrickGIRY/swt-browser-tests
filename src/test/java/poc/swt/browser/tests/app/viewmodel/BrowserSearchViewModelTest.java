package poc.swt.browser.tests.app.viewmodel;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class BrowserSearchViewModelTest {

    @Mock
    private Consumer<ContentEnrichedBySearchResults> onContentEnrichedBySearchResults;

    @Test
    void create_browser_search_view_model() {
        final var browserSearchViewModel = new BrowserSearchViewModel("", onContentEnrichedBySearchResults);

        assertEquals("",browserSearchViewModel.searchText());
        assertFalse(browserSearchViewModel.caseSensitive());
        assertFalse(browserSearchViewModel.wholeWord());
        assertFalse(browserSearchViewModel.nextOccurrenceEnabled());
        assertFalse(browserSearchViewModel.previousOccurrenceEnabled());
        assertEquals("Occurrence 000 / 000", browserSearchViewModel.occurrenceInfos());
    }

    @Test
    void search_one_occurrence_in_text_in_html_document_text_nodes() {
        final var browserSearchViewModel = new BrowserSearchViewModel(Jsoup.parseBodyFragment("""
                <p>This is a test text</p>
                """).html(),
                onContentEnrichedBySearchResults);

        browserSearchViewModel.setSearchText("tes");
        browserSearchViewModel.searchOccurrences();

        then(onContentEnrichedBySearchResults)
                .should()
                        .accept(new ContentEnrichedBySearchResults());

        assertEquals(Jsoup.parseBodyFragment("""
                <p>This is a <span style='background-color: yellow' id='match-1'>tes</span>t  text</p>
                """).html(), browserSearchViewModel.browserText());
        assertFalse(browserSearchViewModel.nextOccurrenceEnabled());
        assertFalse(browserSearchViewModel.previousOccurrenceEnabled());
        assertEquals("Occurrence 001 / 001", browserSearchViewModel.occurrenceInfos());
    }

    @Test
    void search_not_found_in_text_in_html_document_text_nodes() {
        final var originalContent = Jsoup.parseBodyFragment("""
                <p>This is a test text</p>
                """).html();
        final var browserSearchViewModel = new BrowserSearchViewModel(originalContent,
                onContentEnrichedBySearchResults);

        browserSearchViewModel.setSearchText("foo");
        browserSearchViewModel.searchOccurrences();

        then(onContentEnrichedBySearchResults)
                .should()
                .accept(new ContentEnrichedBySearchResults());

        assertEquals(originalContent, browserSearchViewModel.browserText());
        assertFalse(browserSearchViewModel.nextOccurrenceEnabled());
        assertFalse(browserSearchViewModel.previousOccurrenceEnabled());
        assertEquals("Occurrence 000 / 000", browserSearchViewModel.occurrenceInfos());
    }

    @Test
    void search_one_occurrence_of_whole_word_in_text_in_html_document_text_nodes() {
        final var browserSearchViewModel = new BrowserSearchViewModel(Jsoup.parseBodyFragment("""
                <p>This is a test text</p>
                """).html(),
                onContentEnrichedBySearchResults);

        browserSearchViewModel.setSearchText("test");
        browserSearchViewModel.setWholeWord(true);

        then(onContentEnrichedBySearchResults)
                .should()
                        .accept(new ContentEnrichedBySearchResults());

        assertEquals(Jsoup.parseBodyFragment("""
                <p>This is a <span style='background-color: yellow' id='match-1'>test</span> text</p>
                """).html(), browserSearchViewModel.browserText());
        assertFalse(browserSearchViewModel.nextOccurrenceEnabled());
        assertFalse(browserSearchViewModel.previousOccurrenceEnabled());
        assertEquals("Occurrence 001 / 001", browserSearchViewModel.occurrenceInfos());
    }

    @Test
    void search_several_occurrences_in_text_in_html_document_text_nodes() {
        final var browserSearchViewModel = new BrowserSearchViewModel(Jsoup.parseBodyFragment("""
                <p>This is a test text.</p>
                <p>This is an other test text.</p>
                """).html(),
                onContentEnrichedBySearchResults);

        browserSearchViewModel.setSearchText("test");
        browserSearchViewModel.searchOccurrences();

        then(onContentEnrichedBySearchResults)
                .should()
                        .accept(new ContentEnrichedBySearchResults());

        assertEquals(Jsoup.parseBodyFragment("""
                <p>This is a <span style='background-color: yellow' id='match-1'>test</span> text.</p>
                <p>This is an other <span style='background-color: yellow' id='match-2'>test</span> text.</p>
                """).html(), browserSearchViewModel.browserText());

        assertTrue(browserSearchViewModel.nextOccurrenceEnabled());
        assertFalse(browserSearchViewModel.previousOccurrenceEnabled());
        assertEquals("Occurrence 001 / 002", browserSearchViewModel.occurrenceInfos());
    }

    @Test
    void search_several_occurrences_in_text_in_html_document_several_time_must_have_the_same_last_index() {
        final var browserSearchViewModel = new BrowserSearchViewModel(Jsoup.parseBodyFragment("""
                <p>This is a test text.</p>
                <p>This is an other test text.</p>
                """).html(),
                onContentEnrichedBySearchResults);

        browserSearchViewModel.setSearchText("test");
        browserSearchViewModel.searchOccurrences();
        browserSearchViewModel.searchOccurrences();

        assertEquals("Occurrence 001 / 002", browserSearchViewModel.occurrenceInfos());
    }

    @Test
    void search_several_occurrences_in_text_in_html_document_without_distinguished_case_sensitive() {
        final var browserSearchViewModel = new BrowserSearchViewModel(Jsoup.parseBodyFragment("""
                <p>This is a test text.</p>
                <p>This is an other Test text.</p>
                """).html(),
                onContentEnrichedBySearchResults);

        browserSearchViewModel.setSearchText("test");

        browserSearchViewModel.searchOccurrences();

        then(onContentEnrichedBySearchResults)
                .should()
                .accept(new ContentEnrichedBySearchResults());

        assertEquals(Jsoup.parseBodyFragment("""
                <p>This is a <span style='background-color: yellow' id='match-1'>test</span> text.</p>
                <p>This is an other <span style='background-color: yellow' id='match-2'>Test</span> text.</p>
                """).html(), browserSearchViewModel.browserText());
    }

    @Test
    void search_several_occurrences_in_text_in_html_document_with_distinguished_case_sensitive() {
        final var browserSearchViewModel = new BrowserSearchViewModel(Jsoup.parseBodyFragment("""
                <p>This is a test text.</p>
                <p>This is an other Test text.</p>
                """).html(),
                onContentEnrichedBySearchResults);

        browserSearchViewModel.setSearchText("test");

        browserSearchViewModel.setCaseSensitive(true);

        then(onContentEnrichedBySearchResults)
                .should()
                .accept(new ContentEnrichedBySearchResults());

        assertEquals(Jsoup.parseBodyFragment("""
                <p>This is a <span style='background-color: yellow' id='match-1'>test</span> text.</p>
                <p>This is an other Test text.</p>
                """).html(), browserSearchViewModel.browserText());
    }

    @Test
    void previous_occurrence_enabled_when_current_occurrence_is_greater_than_one() {

        final var browserSearchViewModel = new BrowserSearchViewModel(Jsoup.parseBodyFragment("""
                <p>This is a test text.</p>
                <p>This is an other test text.</p>
                """).html(),
                onContentEnrichedBySearchResults);

        browserSearchViewModel.setSearchText("test");
        browserSearchViewModel.searchOccurrences();
        browserSearchViewModel.nextOccurrence();

        assertTrue(browserSearchViewModel.previousOccurrenceEnabled());
        assertEquals("Occurrence 002 / 002", browserSearchViewModel.occurrenceInfos());
    }

    @Test
    void cancel_search_set_browser_text_with_original_content() {

        final var originalContent = Jsoup.parseBodyFragment("""
                <p>This is a test text.</p>
                <p>This is an other test text.</p>
                """).html();

        final var browserSearchViewModel = new BrowserSearchViewModel(originalContent,
                onContentEnrichedBySearchResults);

        browserSearchViewModel.setSearchText("test");
        browserSearchViewModel.searchOccurrences();
        browserSearchViewModel.cancelSearch();

        assertEquals(originalContent, browserSearchViewModel.browserText());
    }
}