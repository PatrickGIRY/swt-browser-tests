package poc.swt.browser.tests.app.viewmodel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class BrowserSearchViewModel {

    private static final Logger LOG = LoggerFactory.getLogger(BrowserSearchViewModel.class);

    private final String originalContent;

    private final Consumer<ContentEnrichedBySearchResults> contentEnrichedBySearchResultsConsumer;

    private String searchText;

    private Pattern searchTextPattern;

    private boolean caseSensitive;

    private boolean wholeWord;

    private boolean nextOccurrenceEnabled;

    private boolean previousOccurrenceEnabled;

    public BrowserSearchViewModel(String originalContent, Consumer<ContentEnrichedBySearchResults> contentEnrichedBySearchResultsConsumer) {
        this.originalContent = originalContent;
        this.contentEnrichedBySearchResultsConsumer = contentEnrichedBySearchResultsConsumer;
    }

    public String searchText() {
        return searchTextPattern != null ? searchTextPattern.pattern() : "";
    }

    public void setSearchText(String searchText) {
        this.searchTextPattern = Pattern.compile(searchText);
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

    public void previousOccurrence() {

    }

    public void cancelSearch() {

    }

    public void searchOccurrences() {

        final var document = Jsoup.parse(this.originalContent);
        int index = 0;
        for (final var node : document.body().childNodes()) {
            processNode(node, index);
        }
        final var enrichedContent = document.html();
        contentEnrichedBySearchResultsConsumer.accept(new ContentEnrichedBySearchResults(enrichedContent));
    }

    private void processNode(Node node, int index) {
        if (node instanceof TextNode textNode) {
            final var text = textNode.text();
            final var processedText = processText(index, text);
            if (!processedText.equals(text)) {
                Element parent = textNode.parent();
                if (parent != null) {
                    parent.html(processedText);
                }
            }
        }
        else if (node instanceof Element element) {
            for (final var childNode : element.childNodes()) {
                processNode(childNode, index);
            }
        }
    }

    private String processText(int index, String text) {
        final var matcher = searchTextPattern.matcher(text);
        final var contentEnrichedBuilder = new StringBuilder();
        while (matcher.find()) {
            String textFound = matcher.group();
            if (textFound != null && !textFound.isEmpty()) {
                LOG.info("Text found \"{}\" ({},{})", textFound, matcher.start(), matcher.end());
                matcher.appendReplacement(contentEnrichedBuilder, wrapText(++index, textFound));
            }
        }
        matcher.appendTail(contentEnrichedBuilder);
        return contentEnrichedBuilder.toString();
    }

    private String wrapText(int index, String text) {
        return String.format("<span style='background-color: yellow' id='match-%d'>%s</span>", index, text.replace("&", "&amp;").replace("<", "&lt;"));
    }
}
