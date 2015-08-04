package org.jruby.mains;

import org.eclipse.jetty.server.Server;

public class JettyStop extends Thread {
    
    private final Server server;
    
    JettyStop(Server server) {
        this.server = server;
    }
    
    public void run() {
        try {
            server.stop();
            server.join();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
