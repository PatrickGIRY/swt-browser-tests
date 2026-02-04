package poc.swt.browser.tests.app.view;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poc.swt.browser.tests.app.viewmodel.BrowserSearchViewModel;
import poc.swt.browser.tests.app.viewmodel.ContentEnrichedBySearchResults;

import java.util.function.Consumer;

public class BrowserSearchDialog extends Dialog {

    private static final Logger LOG = LoggerFactory.getLogger(BrowserSearchDialog.class);

    private final BrowserSearchViewModel viewModel;
    private final Browser browser;
    private Text searchText;
    private Button caseSensitiveButton;
    private Button wholeWordButton;
    private Label occurrenceInfosLabel;
    private Button nextOccurrenceButton;
    private Button previousOccurrenceButton;
    private Button closeButton;

    public BrowserSearchDialog(Browser browser) {
        super(browser.getShell());
        setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE);
        this.browser = browser;
        viewModel = new BrowserSearchViewModel(
                browser.getText(),
                onContentEnrichedBySearchResults(browser));
        browser.addProgressListener(new ProgressAdapter() {
            @Override
            public void completed(ProgressEvent event) {
                LOG.info("Browser search completed");
                scrollToMatch();
            }
        });
    }

    private void scrollToMatch() {
        String script = String.format("""
                let matchElement = document.getElementById('match-%d');
                matchElement.scrollIntoView();
                """, viewModel.currentOccurrenceIndex());
        LOG.info("focus match element script \"{}\"", script);
        browser.execute(script);
        nextOccurrenceButton.setEnabled(viewModel.nextOccurrenceEnabled());
        previousOccurrenceButton.setEnabled(viewModel.previousOccurrenceEnabled());
        occurrenceInfosLabel.setText(viewModel.occurrenceInfos());
    }

    private Consumer<ContentEnrichedBySearchResults> onContentEnrichedBySearchResults(Browser browser) {
        return contentEnrichedBySearchResults -> {
            browser.setText(contentEnrichedBySearchResults.enrichedContent());
            nextOccurrenceButton.setEnabled(viewModel.nextOccurrenceEnabled());
            previousOccurrenceButton.setEnabled(viewModel.previousOccurrenceEnabled());
            occurrenceInfosLabel.setText(viewModel.occurrenceInfos());
        };
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        final var dialogArea = super.createDialogArea(parent);
        if (dialogArea instanceof Composite container) {
            final var layout = container.getLayout();
            if (layout instanceof GridLayout gridLayout) {
                gridLayout.numColumns = 2;
                final var group = new Composite(container, SWT.NONE);
                group.setLayout(new GridLayout(2, false));
                var gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
                gridData.widthHint = 300;
                gridData.verticalIndent = 10;
                group.setLayoutData(gridData);

                searchText = new Text(group, SWT.BORDER);
                searchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
                searchText.addModifyListener(e -> viewModel.setSearchText(searchText.getText()));
                searchText.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        if(e.keyCode == SWT.CR){
                            viewModel.searchOccurrences();
                        }
                    }
                });

                caseSensitiveButton = new Button(group, SWT.CHECK);
                caseSensitiveButton.setText("Case sensitive");
                caseSensitiveButton.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 2, 1));
                caseSensitiveButton.setSelection(viewModel.caseSensitive());
                caseSensitiveButton.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        viewModel.setCaseSensitive(caseSensitiveButton.getSelection());
                    }
                });

                wholeWordButton = new Button(group, SWT.CHECK);
                wholeWordButton.setText("Whole word");
                wholeWordButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 2, 1));
                wholeWordButton.setSelection(viewModel.wholeWord());
                wholeWordButton.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        viewModel.setWholeWord(wholeWordButton.getSelection());
                    }
                });

                occurrenceInfosLabel = new Label(group, SWT.NONE);
                occurrenceInfosLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 2, 1));
                occurrenceInfosLabel.setText(viewModel.occurrenceInfos());

                final var buttonGroup = new Group(container, SWT.NONE);
                buttonGroup.setLayout(new GridLayout(1, false));
                gridData = new GridData(SWT.RIGHT, SWT.FILL, false, false);
                gridData.widthHint = 150;
                buttonGroup.setLayoutData(gridData);

                gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
                nextOccurrenceButton = new Button(buttonGroup, SWT.PUSH);
                nextOccurrenceButton.setText("Next");
                nextOccurrenceButton.setLayoutData(gridData);
                nextOccurrenceButton.setEnabled(viewModel.nextOccurrenceEnabled());
                nextOccurrenceButton.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        viewModel.nextOccurrence();
                        browser.setText(browser.getText());
                    }
                });

                previousOccurrenceButton = new Button(buttonGroup, SWT.PUSH);
                previousOccurrenceButton.setText("Previous");
                previousOccurrenceButton.setLayoutData(gridData);
                previousOccurrenceButton.setEnabled(viewModel.previousOccurrenceEnabled());
                previousOccurrenceButton.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        viewModel.previousOccurrence();
                        browser.setText(browser.getText());
                    }
                });

                closeButton = new Button(buttonGroup, SWT.PUSH);
                closeButton.setText("Cancel");
                closeButton.setLayoutData(gridData);
                closeButton.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        viewModel.cancelSearch();
                    }
                });
            }
        }

        return dialogArea;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {

    }
}
