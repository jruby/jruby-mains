package de.saumya.mojo.mains;

import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Properties;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyRunMain {
    
    public static void main(String[] args) {
        main(System.getProperties(), args);
    }
    
    public static void main(Properties props, String[] args) {
        Server server = new Server();
        SocketConnector connector = new SocketConnector();
        
        // Set some timeout options to make debugging easier.
        connector.setMaxIdleTime(1000 * 60 * 60);
        connector.setSoLingerTime(-1);
        connector.setPort(Integer.parseInt(props.getProperty("port", "8989")));
        connector.setHost(props.getProperty("host"));
        server.setConnectors(new Connector[] { connector });
        
        WebAppContext context = new WebAppContext();
        context.setServer(server);
        context.setContextPath("/");
        context.setExtractWAR(false);
        context.setCopyWebInf(true);
        
        ProtectionDomain protectionDomain = JettyRunMain.class
                .getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        context.setWar(location.toExternalForm());
        
        server.setHandler(context);
        Runtime.getRuntime().addShutdownHook(new JettyStop(server));
        try {
            server.start();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
    }
}
