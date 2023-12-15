package bichel.yauhen.web.jetty.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import bichel.yauhen.web.exception.HttpHandlerNotFoundException;

import javax.servlet.http.HttpServlet;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/** This class uses Jetty & servlets to implement server responding to Http GET requests */
public class JettyServer {
    private static final Logger logger = LogManager.getLogger(JettyServer.class);

    private static final int PORT = 8090;
    private final Server jettyServer;
    private final ServletHandler handler;

    public JettyServer() {
        jettyServer = new Server(PORT);
        handler = new ServletHandler();
        jettyServer.setHandler(handler);
    }

    /**
     * Maps a given URL path/endpoint to the name of the servlet class that will handle requests coming at this endpoint
     * @param path end point
     * @param handlerClass  the servlet class
     */
    public void addHandler(String path, Class handlerClass, Class resourceType, Object resource) {
        logger.info("Added servlet: " + handlerClass.getName());
        try {
            Constructor c = Class.forName(handlerClass.getName()).getConstructor(resourceType);
            HttpServlet httpServlet = (HttpServlet) c.newInstance(resource);
            handler.addServletWithMapping(new ServletHolder(httpServlet), path);
        }  catch (InstantiationException | IllegalAccessException |
                ClassNotFoundException | InvocationTargetException |
                NoSuchMethodException ex) {
            logger.error(ex);
            throw new HttpHandlerNotFoundException("The http servlet is not found for the request", ex);
        }
    }

    /**
     * Function that starts the server
     * @throws Exception throws exception if access failed
     */
    public  void start() throws Exception {
        logger.info("Starting Jetty server");

        jettyServer.setHandler(handler);
        jettyServer.start();
        jettyServer.join();
    }
}
