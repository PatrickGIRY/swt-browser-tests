package poc.swt.browser.tests.app;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class DocumentView extends Composite {
    private Browser browser;

    public DocumentView(Composite parent, int style) {
        super(parent, style);
        setLayout(new FillLayout());
        browser = new Browser(this, SWT.NONE);
        browser.setUrl("about:blank");
    }

    public Browser getBrowser() {
        return browser;
    }
}
