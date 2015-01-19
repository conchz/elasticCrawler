package org.elasticcrawler.downloader;

import org.elasticcrawler.core.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by dolphineor on 2015-1-17.
 * <p>
 * 基于淘宝的定制型下载器(淘宝采用的是KISSY javascript框架, htmlUnit无法识别到)
 */
public class KissyDownloader implements Downloader {

    private static Downloader downloader = null;


    private KissyDownloader() {
    }

    @Override
    public String download(Task task) throws IOException {
        Path scriptPath = JavascriptHelper.getScriptPath();
        String cmd = "phantomjs " + scriptPath + " " + task.getUrl();
        Process process = Runtime.getRuntime().exec(cmd);

        StringBuilder html = new StringBuilder("");

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        br.lines().forEach(html::append);
        return html.toString();
    }

    @Override
    public String getName() {
        return KISSY_DOWNLOADER;
    }

    public static Downloader create() {
        // DCL
        if (downloader == null) {
            synchronized (KissyDownloader.class) {
                if (downloader == null)
                    downloader = new KissyDownloader();
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
