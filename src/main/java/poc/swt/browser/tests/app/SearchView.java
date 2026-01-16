package poc.swt.browser.tests.app;

import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.nio.file.Paths;

public class SearchView {
    private final Display display;
    private Shell shell;
    private Text searchText;
    private Browser browser;

    public SearchView(Display display) {
        this.display = display;
        createContents();
    }

    private void createContents() {
        shell = new Shell(display);
        shell.setSize(800, 600); // Largeur x Hauteur en pixels
        // Ou pour une taille minimale :
        shell.setMinimumSize(800, 600);
        shell.setLayout(new GridLayout(1, false));

        // Champ de recherche
        searchText = new Text(shell, SWT.BORDER);
        searchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        searchText.setMessage("Rechercher du texte...");

        // Bouton de recherche
        Button searchButton = new Button(shell, SWT.PUSH);
        searchButton.setText("Rechercher");
        searchButton.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
        searchButton.addListener(SWT.Selection, e -> performSearch());

        // Navigateur
        browser = new Browser(shell, SWT.BORDER);
        browser.setSize(shell.getSize());
        browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        // Charger une page par défaut
        browser.setUrl(Paths.get("src/main/resources/document.html").toUri().toString());
    }

    private void performSearch() {
        String searchTerm = searchText.getText();
        if (searchTerm.isEmpty()) return;

        String htmlContent = browser.getText();
        String highlightedContent = highlightText(htmlContent, searchTerm);
        browser.setText(highlightedContent);
    }

    private String highlightText(String html, String searchTerm) {
        // Simple regex pour trouver le texte (à améliorer pour le HTML)
        return html.replaceAll(searchTerm, "<span style='background-color: yellow'>" + searchTerm + "</span>");
    }

    public void open() {
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
}

