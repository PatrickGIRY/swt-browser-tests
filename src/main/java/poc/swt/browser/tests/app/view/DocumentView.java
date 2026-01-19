package poc.swt.browser.tests.app.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import poc.swt.browser.tests.app.viewmodel.DocumentViewModel;

public class DocumentView extends Composite {

    private final Browser browser;
    private final  Text urlText;
    private final DocumentViewModel viewModel;

    public DocumentView(Composite parent, int style) {
        super(parent, style);
        this.viewModel = new DocumentViewModel();
        setLayout(new GridLayout(1, false));

        // Champ de texte pour l'URL
        urlText = new Text(this, SWT.BORDER);
        urlText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        urlText.setText(viewModel.currentUrl());
        urlText.addListener(SWT.KeyDown, e -> {
            if (e.keyCode == SWT.CR) {
                loadUrl();
            }
        });

        // Navigateur
        browser = new Browser(this, SWT.NONE);
        browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        browser.setUrl(viewModel.currentUrl());
    }

    private void loadUrl() {
        String url = urlText.getText();
        if (url != null && !url.isEmpty()) {
            viewModel.loadUrl(url);
            browser.setUrl(viewModel.currentUrl());
        }
    }
}
