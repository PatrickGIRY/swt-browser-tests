package poc.swt.browser.tests.app;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poc.swt.browser.tests.app.view.DocumentView;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.info("Starting SWT Browser Application");
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        shell.setSize(800, 600);
        shell.setText("SWT Browser Application");

        DocumentView documentView = new DocumentView(shell, SWT.NONE);

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        documentView.dispose();
        display.dispose();
    }
}
