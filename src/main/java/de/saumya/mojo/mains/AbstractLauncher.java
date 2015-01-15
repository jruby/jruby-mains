package de.saumya.mojo.mains;

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
        
        String workingDir = processJar(jarLocation(), urls);
        
        launchIt(urls, workingDir, args);
    }
    
    abstract String processJar(URL location, List<URL> urls) throws IOException;
    
    URL jarLocation() {
        ProtectionDomain protectionDomain = AbstractLauncher.class
                .getProtectionDomain();
        return protectionDomain.getCodeSource().getLocation();
    }
    
    void launchIt(List<URL> classloaderUrls, String workingDir, String... args)
            throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, IOException {
        // we want to have a clean classloader hierarchy without this
        // classloader involved
        try (URLClassLoader loader = new URLClassLoader(
                classloaderUrls.toArray(new URL[classloaderUrls.size()]),
                ClassLoader.getSystemClassLoader().getParent())) {
            Class<?> main = loader.loadClass("de.saumya.mojo.mains.JRubyMain");
            Method m = main.getMethod("main", String.class, args.getClass());
            m.invoke(main, workingDir, (Object[]) args);
        }
    }
}
