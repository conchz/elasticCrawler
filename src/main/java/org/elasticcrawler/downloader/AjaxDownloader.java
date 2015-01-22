package org.elasticcrawler.downloader;

import org.elasticcrawler.scheduler.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by dolphineor on 2015-1-20.
 */
public class AjaxDownloader implements Downloader {

    private static final String PHANTOMJS = "phantomjs";

    private static Path scriptPath = null;
    private static Downloader downloader = null;


    private AjaxDownloader() {
        init();
    }

    @Override
    public String download(Task task) throws IOException {
        String cmd = PHANTOMJS + " " + scriptPath + " " + task.getUrl();
        Process process = Runtime.getRuntime().exec(cmd);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            StringBuilder html = new StringBuilder();
            br.lines().parallel().forEach(html::append);
            return html.toString();
        }
    }

    @Override
    public String getName() {
        return AJAX_DOWNLOADER;
    }

    private void init() {
        if (scriptPath == null) {
            synchronized (this) {
                if (scriptPath == null) {
                    try {
                        scriptPath = JavascriptHelper.getScriptPath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static Downloader create() {
        // DCL
        if (downloader == null) {
            synchronized (AjaxDownloader.class) {
                if (downloader == null)
                    downloader = new AjaxDownloader();
            }
        }
        return downloader;
    }


    /**
     * based on phantomjs
     */
    static final class JavascriptHelper {

        private static final String TMP_DIR = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator");

        private static final String JAVASCRIPT_PREFIX = "crawl";

        private static final String JAVASCRIPT_SUFFIX = ".js";

        private static final byte[] JAVASCRIPT_CONTENT = ("var system = require('system');\n" +
                "var url = system.args[1];\n" +
                "\n" +
                "var page = require('webpage').create();\n" +
                "page.settings.loadImages = false;\n" +
                "page.settings.userAgent = 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.99 Safari/537.36';\n" +
                "page.settings.resourceTimeout = 5000;\n" +
                "\n" +
                "page.open(url, function (status) {\n" +
                "    if (status != 'success') {\n" +
                "        console.log(\"HTTP request failed!\");\n" +
                "    } else {\n" +
                "        console.log(page.content);\n" +
                "    }\n" +
                "\n" +
                "    page.close();\n" +
                "    phantom.exit();\n" +
                "});").getBytes();

        private static final Path JAVASCRIPT_PATH = Paths.get(TMP_DIR + JAVASCRIPT_PREFIX + JAVASCRIPT_SUFFIX);


        public static Path getScriptPath() throws IOException {
            if (Files.notExists(JAVASCRIPT_PATH))
                Files.write(JAVASCRIPT_PATH, JAVASCRIPT_CONTENT);
            return JAVASCRIPT_PATH;
        }

        public static boolean deleteScript(Path path) throws IOException {
            return Files.deleteIfExists(path);
        }

    }

}
