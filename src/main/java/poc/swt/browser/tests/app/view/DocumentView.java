package poc.swt.browser.tests.app.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DocumentView extends Composite {

    private Browser browser;
    private Text urlText;

    public DocumentView(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout(1, false));

        // Champ de texte pour l'URL
        urlText = new Text(this, SWT.BORDER);
        urlText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        urlText.setText("about:blank");

        // Bouton pour charger l'URL
        Button loadButton = new Button(this, SWT.PUSH);
        loadButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
        loadButton.setText("Charger");
        loadButton.addListener(SWT.Selection, e -> loadUrl());

        // Navigateur
        browser = new Browser(this, SWT.NONE);
        browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        browser.setUrl("about:blank");
    }

    private void loadUrl() {
        String url = urlText.getText();
        if (url != null && !url.isEmpty()) {
            browser.setUrl(url);
        }
    }
}
