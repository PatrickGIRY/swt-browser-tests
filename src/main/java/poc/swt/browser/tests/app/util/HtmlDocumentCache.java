package poc.swt.browser.tests.app.util;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public final class HtmlDocumentCache {

    private HtmlDocumentCache() {
    }

    private static final Logger LOG = LoggerFactory.getLogger(HtmlDocumentCache.class);

    public static String cacheDocument(String url) {
        try {
            final var document = Jsoup.parse(new URL(url), 2000);
            final var htmlContent = document.html();
            Path documentViewCachePath = Files.createTempFile("documentViewCache", ".html");
            Files.writeString(documentViewCachePath, htmlContent);
            LOG.info("Document view cache path: {}", documentViewCachePath);
            return documentViewCachePath.toUri().toString();
        } catch (IOException e) {
            LOG.error("Error when parse document", e);
            throw new RuntimeException(e);
        }
    }
}
