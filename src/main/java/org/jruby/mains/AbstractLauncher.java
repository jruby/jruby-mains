package org.jruby.mains;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractLauncher {
    
    void launch(String... args) throws Exception {
        List<URL> urls = new LinkedList<URL>();
        
        Config config = processJar(jarLocation(), urls);

        launchIt(urls, config, args);
    }
    
    abstract Config processJar(URL location, List<URL> urls) throws IOException;
    
    URL jarLocation() {
        ProtectionDomain protectionDomain = AbstractLauncher.class
                .getProtectionDomain();
        return protectionDomain.getCodeSource().getLocation();
    }
    
    void launchIt(List<URL> classloaderUrls, Config config, String... args)
            throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, IOException {
        //TODO debug:
	//System.err.println(classloaderUrls);
        //System.err.println(config);
        // we want to have a clean classloader hierarchy without this
        // classloader involved
        try (URLClassLoader loader = new URLClassLoader(
                classloaderUrls.toArray(new URL[classloaderUrls.size()]),
                ClassLoader.getSystemClassLoader().getParent())) {
            Class<?> main = loader.loadClass("org.jruby.mains.JRubyMain");
            Method m = main.getMethod("main", String.class, String.class, String.class, args.getClass());
            m.invoke(main, config.bundleDisableSharedGems, config.workingDirectory, config.jrubyHome, (Object[]) args);
        }
    }
}
