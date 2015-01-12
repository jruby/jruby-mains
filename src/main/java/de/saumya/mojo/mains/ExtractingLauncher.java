package de.saumya.mojo.mains;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

class ExtractingLauncher extends AbstractLauncher {
	File processJar(String classesDir, String jarsDir, URL location,
		List<URL> urls) throws IOException, MalformedURLException {
		final File dir;
		try(InputStream is = location.openStream()){
			ExtractedZip extractedZip = new ExtractedZip(is);
			urls.add(extractedZip.directory().toURI().toURL());
			if (classesDir == null) {
				dir = extractedZip.directory();
			}
			else {
				dir = new File(extractedZip.directory(), classesDir);
				urls.add(dir.toURI().toURL());
			}
			if (jarsDir != null) {
				for(File f: new File(extractedZip.directory(), jarsDir).listFiles()) {
					if (f.getName().endsWith(".jar")) {
						urls.add(f.toURI().toURL());
					}
				}
			}
		}
		return dir;
	}
}