package poc.swt.browser.tests.app.viewmodel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.regex.Pattern;

public class BrowserSearchViewModel {

    private static final Logger LOG = LoggerFactory.getLogger(BrowserSearchViewModel.class);

    private final String originalContent;

    private String browserText;

    private final Consumer<ContentEnrichedBySearchResults> contentEnrichedBySearchResultsConsumer;

    private Pattern searchTextPattern;

    private boolean caseSensitive;

    private boolean wholeWord;

    private int currentOccurrenceIndex;
    private int lastOccurrenceIndex;


    public BrowserSearchViewModel(String originalContent, Consumer<ContentEnrichedBySearchResults> contentEnrichedBySearchResultsConsumer) {
        this.originalContent = originalContent;
        this.contentEnrichedBySearchResultsConsumer = contentEnrichedBySearchResultsConsumer;
        this.currentOccurrenceIndex = 0;
        this.lastOccurrenceIndex = 0;
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

    public String browserText() {
        return browserText;
    }

    public boolean nextOccurrenceEnabled() {
        return currentOccurrenceIndex > 0 && currentOccurrenceIndex < lastOccurrenceIndex;
    }

    public boolean previousOccurrenceEnabled() {
        return currentOccurrenceIndex > 1;
    }

    public int currentOccurrenceIndex() {
        return currentOccurrenceIndex;
    }

    public String occurrenceInfos() {
        return String.format("Occurrence %03d / %03d", currentOccurrenceIndex, lastOccurrenceIndex);
    }

    public void nextOccurrence() {
        if (currentOccurrenceIndex < lastOccurrenceIndex) {
            currentOccurrenceIndex++;
        }
    }

    public void previousOccurrence() {
        if (currentOccurrenceIndex > 1) {
            currentOccurrenceIndex--;
        }
    }

    public void cancelSearch() {
        this.browserText = originalContent;
    }

    public void searchOccurrences() {
        this.browserText = enrichContent();
        this.currentOccurrenceIndex = 1;
        contentEnrichedBySearchResultsConsumer.accept(new ContentEnrichedBySearchResults());
    }

    private String enrichContent() {
        this.lastOccurrenceIndex = 0;
        final var document = Jsoup.parse(this.originalContent);
        for (final var node : document.body().childNodes()) {
            processNode(node);
        }
        return document.html();
    }

    private void processNode(Node node) {
        if (node instanceof TextNode textNode) {
            final var text = textNode.text();
            final var processedText = processText(text);
            if (!processedText.equals(text)) {
                Element parent = textNode.parent();
                if (parent != null) {
                    parent.html(processedText);
                }
            }
        }
        else if (node instanceof Element element) {
            for (final var childNode : element.childNodes()) {
                processNode(childNode);
            }
        }
    }

    private String processText(String text) {
        final var matcher = searchTextPattern.matcher(text);
        final var contentEnrichedBuilder = new StringBuilder();
        while (matcher.find()) {
            String textFound = matcher.group();
            if (textFound != null && !textFound.isEmpty()) {
                LOG.info("Text found \"{}\" ({},{})", textFound, matcher.start(), matcher.end());
                matcher.appendReplacement(contentEnrichedBuilder, wrapText(++lastOccurrenceIndex, textFound));
            }
        }
        matcher.appendTail(contentEnrichedBuilder);
        return contentEnrichedBuilder.toString();
    }

    private String wrapText(int index, String text) {
        return String.format("<span style='background-color: yellow' id='match-%d'>%s</span>", index, text.replace("&", "&amp;").replace("<", "&lt;"));
    }
}
