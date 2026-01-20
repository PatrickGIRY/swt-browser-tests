package poc.swt.browser.tests.app.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import poc.swt.browser.tests.app.eventbus.EventBus;
import poc.swt.browser.tests.app.viewmodel.CurrentUrlUpdated;
import poc.swt.browser.tests.app.viewmodel.DocumentViewModel;
import poc.swt.browser.tests.app.viewmodel.Event;

import java.util.function.Consumer;

public class DocumentView extends Composite {

    private final Browser browser;
    private final  Text urlText;
    private final DocumentViewModel viewModel;
    private final EventBus<Event> eventBus;
    private final Consumer<CurrentUrlUpdated> currentUrlUpdatedConsumer;

    public DocumentView(Composite parent, int style) {
        super(parent, style);
        this.eventBus = new EventBus<>();
        this.viewModel = new DocumentViewModel(eventBus);
        setLayout(new GridLayout(1, false));

        // Champ de texte pour l'URL
        urlText = new Text(this, SWT.BORDER);
        urlText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        urlText.setText(viewModel.currentUrl());
        urlText.addListener(SWT.KeyDown, e -> {
            if (e.keyCode == SWT.CR) {
                viewModel.setCurrentUrl(urlText.getText());
            }
        });

        // Navigateur
        browser = new Browser(this, SWT.NONE);
        browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        browser.setUrl(viewModel.currentUrl());

        currentUrlUpdatedConsumer = this::onCurrentUrlUpdated;
        eventBus.subscribe(CurrentUrlUpdated.class, currentUrlUpdatedConsumer);

        browser.addProgressListener(new  ProgressListener() {
            @Override
            public void changed(ProgressEvent event) {

            }

            @Override
            public void completed(ProgressEvent event) {
                viewModel.setNewContent(browser.getUrl(), browser.getText());
            }
        });
    }

    private void onCurrentUrlUpdated(CurrentUrlUpdated event) {
        browser.setUrl(event.newUrl());
    }

    @Override
    public void dispose() {
        super.dispose();
        eventBus.unsubscribe(CurrentUrlUpdated.class, currentUrlUpdatedConsumer);
    }
}
