package org.jruby.mains;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ExtractingMain extends AbstractLauncher {
    
    public static void main(String... args) throws Exception {
        new ExtractingMain().launch(args);
    }
    
    Config processJar(URL jarFile, List<URL> urls) throws IOException {
        final File dir;
        try (InputStream is = jarFile.openStream()) {
            ExtractedZip extractedZip = new ExtractedZip(is);
            urls.add(extractedZip.directory().toURI().toURL());
            File jarsDir = new File(extractedZip.directory(), "WEB-INF/lib");
            if (jarsDir.isDirectory()) {
                dir = new File(extractedZip.directory(), "WEB-INF/classes");
                urls.add(dir.toURI().toURL());
                for (File f : jarsDir.listFiles()) {
                    if (f.getName().endsWith(".jar")) {
                        urls.add(f.toURI().toURL());
                    }
                }
            }
            else {
                dir = extractedZip.directory();
            }
        }
        return new Config(dir.getAbsolutePath());
    }
}