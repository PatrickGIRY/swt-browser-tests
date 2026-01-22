package poc.swt.browser.tests.app.util;

public final class OSUtils {

    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    private OSUtils() {
    }

    public static boolean isWindows() {
        return OS_NAME.contains("win");
    }
    public static boolean isMac() {
        return OS_NAME.contains("mac");
    }

    public static boolean isLinux() {
        return OS_NAME.contains("linux");
    }
}
