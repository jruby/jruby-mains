package de.saumya.mojo.mains;

public class Config {
    
    final String workingDirectory;

    final String jrubyHome;

    final String bundleDisableSharedGems;
    
    Config( String workingDirectory ) {
    	this( workingDirectory, 
    	      workingDirectory + "/META-INF/jruby.home", true );
    }

    Config( String workingDirectory, String jrubyHome, Boolean bundleDisableSharedGems ) {
        this.workingDirectory = workingDirectory;
        this.jrubyHome = jrubyHome;
        this.bundleDisableSharedGems = bundleDisableSharedGems == null ? null : bundleDisableSharedGems.toString();
    }
}
