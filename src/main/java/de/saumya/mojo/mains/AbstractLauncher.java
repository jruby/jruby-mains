package de.saumya.mojo.mains;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractLauncher  {

	void main(String classesDir, String jarsDir, String... args) throws Exception {
		URL location = jarLocation();
	
		List<URL> urls = new LinkedList<URL>();
		
		File workingDir = processJar(classesDir, jarsDir, location, urls);

		launchIt(urls, workingDir, args);
	}

	abstract File processJar(String classesDir, String jarsDir, URL location,
			List<URL> urls) throws IOException;

	URL jarLocation() {
		ProtectionDomain protectionDomain = AbstractLauncher.class.getProtectionDomain();
		return protectionDomain.getCodeSource().getLocation();
	}

	void launchIt(List<URL> classloaderUrls, final File workingDir, String... args)
			throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, IOException {
		// we want to have a clean classloader hierarchy without this classloader involved
		try(URLClassLoader loader = new URLClassLoader(classloaderUrls.toArray(new URL[classloaderUrls.size()]),
				ClassLoader.getSystemClassLoader().getParent())) {
			Class<?> main = loader.loadClass("de.saumya.mojo.mains.JRubyMain");
			Method m = main.getMethod("main", String.class, args.getClass());
			m.invoke(main, workingDir.getAbsolutePath(), (Object[]) args);
		}
	}
}
