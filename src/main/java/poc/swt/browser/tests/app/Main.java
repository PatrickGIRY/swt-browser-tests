package poc.swt.browser.tests.app;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.info("Starting SWT Browser Application");
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        shell.setSize(800, 600);
        shell.setText("SWT Browser Application");

        Browser browser = new Browser(shell, SWT.NONE);
        browser.setUrl("about:blank");
        browser.setText("<html><body><h1>Document HTML</h1><p>Ceci est un exemple de document HTML affiché dans SWT.</p></body></html>");

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
        LOG.info("SWT Browser Application closed");
    }
}
