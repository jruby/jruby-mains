package de.saumya.mojo.mains;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class WarMain extends AbstractLauncher {

	public static void main(String... args) throws Exception {
		new WarMain().launch(args);
	}

	String processJar(URL jarFile, List<URL> urls) throws IOException {
		urls.add(jarFile);
		try(InputStream is = jarFile.openStream()){
			ExtractedZip extractedZip = new ExtractedZip(is, true);
			urls.addAll(extractedZip.urls());
        }
		return "uri:classloader://WEB-INF/classes";
	}
}