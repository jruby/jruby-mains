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

    public static final String JAR_BOOTSTRAP = "jar-bootstrap.rb";
    public static final String META_INF_JAR_BOOTSTRAP = "META-INF/jar-bootstrap.rb";

    private static String[] addBootstrap(String... args){
        String bootstrap = null;
        if (JRubyMain.class.getClassLoader().getResource(JAR_BOOTSTRAP) != null) {
            bootstrap = "classpath:" + JAR_BOOTSTRAP;
        }
        else if (JRubyMain.class.getClassLoader().getResource(META_INF_JAR_BOOTSTRAP) != null) {
            bootstrap = "classpath:" + META_INF_JAR_BOOTSTRAP;
        }
        if (bootstrap == null) {
            return args;
        }
        String[] newArgs = new String[args.length + 1];
        newArgs[0] = bootstrap;
        System.arraycopy(args, 0, newArgs, 1, args.length);
        return newArgs;
    }

    public static void main(String bundleDisableSharedGems, String currentDir, String jrubyHome, String... args) {
        JRubyMain main = new JRubyMain(bundleDisableSharedGems, currentDir, jrubyHome);
        try {
            Status status = main.run(addBootstrap(args));
            if (status.isExit()) {
                System.exit(status.getStatus());
            }
        }
        catch (RaiseException rj) {
            try {
                System.exit(-1);//handleRaiseException(rj));
            }
            catch( Throwable e ){
                System.exit(-1);
            }
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
    
    JRubyMain(String bundleDisableSharedGems, String currentDirectory, String jrubyHome) {
        this(bundleDisableSharedGems, currentDirectory, jrubyHome, new RubyInstanceConfig());
    }
    
    JRubyMain(String bundleDisableSharedGems, String currentDirectory, String jrubyHome, RubyInstanceConfig config) {
        super(config);
        // TODO have property to disable hard exit - see warbler
        config.setHardExit(true);
        config.setCurrentDirectory(currentDirectory + "/");
        config.setJRubyHome(jrubyHome);
        Map<String, String> env = new HashMap<String, String>(System.getenv());
        // we assume the embedded jars are placed in jars directory
        env.put("JARS_HOME", currentDirectory + "/jars");
        // we assume the embedded gems are placed at the root of the "archive"
        env.put("GEM_PATH", currentDirectory + "/");
        // make sure we do not inherit it from outside
        // NOTE: setting it to GEM_PATH will break the extractingMain cases 
        env.put("GEM_HOME", jrubyHome + "/lib/ruby/gems/shared");

        if (bundleDisableSharedGems != null) {
            // for spawning jruby we need bundler to tell to
            // NOT clean up the load Path
            env.put("BUNDLE_DISABLE_SHARED_GEMS", bundleDisableSharedGems);
        }

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
        addRequire(newArgs, "META-INF/monkey_patches");
        return super.run(newArgs.toArray(new String[newArgs.size()]));
    }
    
    private void addRequire(List<String> newArgs, String name) {
        if (getClass().getClassLoader().getResource(name + ".rb") != null) {
            newArgs.add(0, "-r" + name);
        }
    }
}
