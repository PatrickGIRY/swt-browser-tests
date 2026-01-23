package poc.swt.browser.tests.app.viewmodel;

import java.util.function.Consumer;

public class BrowserSearchViewModel {

    private final String originalContent;

    private final Consumer<ContentEnrichedBySearchResults> contentEnrichedBySearchResultsConsumer;

    private String searchText;

    private boolean caseSensitive;

    private boolean wholeWord;

    private boolean nextOccurrenceEnabled;

    private boolean previousOccurrenceEnabled;

    public BrowserSearchViewModel(String originalContent, Consumer<ContentEnrichedBySearchResults> contentEnrichedBySearchResultsConsumer) {
        this.originalContent = originalContent;
        this.contentEnrichedBySearchResultsConsumer = contentEnrichedBySearchResultsConsumer;
    }

    public String searchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public boolean caseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public boolean wholeWord() {
        return wholeWord;
    }

    public void setWholeWord(boolean wholeWord) {
        this.wholeWord = wholeWord;
    }

    public boolean nextOccurrenceEnabled() {
        return nextOccurrenceEnabled;
    }

    public void setNextOccurrenceEnabled(boolean nextOccurrenceEnabled) {
        this.nextOccurrenceEnabled = nextOccurrenceEnabled;
    }

    public boolean previousOccurrenceEnabled() {
        return previousOccurrenceEnabled;
    }

    public void setPreviousOccurrenceEnabled(boolean previousOccurrenceEnabled) {
        this.previousOccurrenceEnabled = previousOccurrenceEnabled;
    }

    public void nextOccurrence() {

    }

    public void previousOccurence() {

    }

    public void cancelSearch() {

    }

    public void searchOccurrences() {

    }
}
