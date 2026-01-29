package poc.swt.browser.tests.app.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import poc.swt.browser.tests.app.util.OSUtils;
import poc.swt.browser.tests.app.viewmodel.LocationUpdated;
import poc.swt.browser.tests.app.viewmodel.DocumentViewModel;

public class DocumentView extends Composite  {
    private final Text addressBarText;
    private final Browser browser;
    private final DocumentViewModel viewModel;

    public DocumentView(Composite parent, int style) {
        super(parent, style);
        this.viewModel = new DocumentViewModel(this::onCurrentUrlUpdated);
        setLayout(new GridLayout(1, false));

        // Champ de texte pour l'URL
        addressBarText = new Text(this, SWT.BORDER);
        addressBarText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        addressBarText.setText(viewModel.addressBarText());
        addressBarText.addModifyListener(e -> viewModel.setAddressBarText(addressBarText.getText()));
        addressBarText.addListener(SWT.KeyDown, e -> {
            if (e.keyCode == SWT.CR) {
                viewModel.changeLocation();
            }
        });

        // Navigateur
        browser = new Browser(this, SWT.NONE);
        browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        browser.setUrl(viewModel.browserUrl());
        browser.setText(viewModel.browserText());
        browser.addProgressListener(new ProgressAdapter() {

            @Override
            public void completed(ProgressEvent event) {
                viewModel.setBrowserUrl(browser.getUrl());
                viewModel.setBrowserText(browser.getText());
            }
        });

        browser.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.keyCode == 'f' && isCtrlPressed(e)) {
                    openSearchDialog();
                }
            }
        });
    }

    private void openSearchDialog() {
        final var browserSearchDialog = new BrowserSearchDialog(browser);
        browserSearchDialog.open();
    }

    private boolean isCtrlPressed(KeyEvent e) {
        return OSUtils.isMac() ? (e.stateMask & SWT.COMMAND) != 0 : (e.stateMask & SWT.CTRL) != 0;
    }

    private void onCurrentUrlUpdated(LocationUpdated event) {
        browser.setUrl(viewModel.browserUrl());
        addressBarText.setText(viewModel.addressBarText());
    }
}
