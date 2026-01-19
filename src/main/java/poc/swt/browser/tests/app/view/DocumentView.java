package poc.swt.browser.tests.app.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class DocumentView extends Composite {

    public DocumentView(Composite parent, int style) {
        super(parent, style);
        setLayout(new FillLayout());
        Browser browser = new Browser(this, SWT.NONE);
        browser.setUrl("about:blank");
    }

}
