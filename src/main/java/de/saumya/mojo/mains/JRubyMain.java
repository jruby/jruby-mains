package de.saumya.mojo.mains;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jruby.Main;
import org.jruby.RubyInstanceConfig;
import org.jruby.exceptions.RaiseException;
import org.jruby.runtime.ThreadContext;

public class JRubyMain extends Main {
    
    public static void main(String currentDir, String... args) {
        JRubyMain main = new JRubyMain(currentDir);
        try {
            Status status = main.run(args);
            if (status.isExit()) {
                System.exit(status.getStatus());
            }
        }
        catch (RaiseException rj) {
            System.exit(-1);// TODO handleRaiseException(rj));
        }
        catch (Throwable t) {
            // print out as a nice Ruby backtrace
            System.err.println(ThreadContext
                    .createRawBacktraceStringFromThrowable(t));
            while ((t = t.getCause()) != null) {
                System.err.println("Caused by:");
                System.err.println(ThreadContext
                        .createRawBacktraceStringFromThrowable(t));
            }
            System.exit(1);
        }
    }
    
    JRubyMain(String currentDirectory) {
        this(currentDirectory, new RubyInstanceConfig());
    }
    
    JRubyMain(String currentDirectory, RubyInstanceConfig config) {
        super(config);
        // TODO have property to disable hard exit - see warbler
        config.setHardExit(true);
        config.setCurrentDirectory(currentDirectory);
        config.setJRubyHome(currentDirectory + "/META-INF/jruby.home");
        Map<String, String> env = new HashMap<String, String>(System.getenv());
	// we assume the embedded jars are placed at the root of the "archive"
        env.put("JARS_HOME", currentDirectory);
        // we assume the embedded gems are placed at the root of the "archive"
        env.put("GEM_PATH", currentDirectory);

	// for spawning jruby we need bundler to tell to 
	// NOT clean up the load Path
	env.put("BUNDLE_DISABLE_SHARED_GEMS", "true");

        config.setEnvironment(env);
        // older jruby-1.7.x does need this
        config.setLoader(JRubyMain.class.getClassLoader());
    }
    
    public Status run(String[] args) {
        // require META-INF/init.rb and WEB-INF/init.rb to load any additional
        // config
        // following warblers and jruby-rack convention
        List<String> newArgs = new ArrayList<String>(Arrays.asList(args));
        addRequire(newArgs, "META-INF/init");
        addRequire(newArgs, "WEB-INF/init");
        addRequire(newArgs, "META-INF/bundler_shared_helpers_patch");
        return super.run(newArgs.toArray(new String[newArgs.size()]));
    }
    
    private void addRequire(List<String> newArgs, String name) {
        if (getClass().getClassLoader().getResource(name + ".rb") != null) {
            newArgs.add(0, "-r" + name);
        }
    }
}
