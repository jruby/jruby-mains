package de.saumya.mojo.mains;

public class JarMain {
    
    public static void main(String... args) {
        JRubyMain.main(null, "uri:classloader:/", "uri:classloader://META-INF/jruby.home", args);
    }
    
}
