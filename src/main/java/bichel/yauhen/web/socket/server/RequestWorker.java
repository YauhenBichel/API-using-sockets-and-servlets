package bichel.yauhen.web.socket.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bichel.yauhen.web.exception.HttpHandlerNotFoundException;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

/**
 * Worker for each request
 */
public final class RequestWorker implements Runnable {
    private static final Logger logger = LogManager.getLogger(RequestWorker.class);

    private final String httpHandlerName;
    private final Object resource;
    private final Class resourceType;
    private final HttpRequest httpRequest;
    private final HttpResponse httpResponse;
    private final Socket connectionSocket;

    public RequestWorker(String httpHandlerName, Object resource,
                         Class resourceType,
                         HttpRequest httpRequest, HttpResponse httpResponse,
                         Socket connectionSocket) {
        this.httpHandlerName = httpHandlerName;
        this.resource = resource;
        this.resourceType = resourceType;
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
        this.connectionSocket = connectionSocket;
    }

    /**
     * Runnable method for handling request
     */
    @Override
    public void run() {
        HttpHandler httpHandler;
        try {
            Constructor c = Class.forName(httpHandlerName).getConstructor(resourceType);
            httpHandler = (HttpHandler) c.newInstance(resource);
            httpHandler.processRequest(httpRequest, httpResponse);
        } catch (InstantiationException | IllegalAccessException |
                ClassNotFoundException | InvocationTargetException |
                NoSuchMethodException ex) {
            logger.error(ex);
            throw new HttpHandlerNotFoundException("The handler is not found for the request", ex);
        } finally {
            if (connectionSocket != null && !connectionSocket.isClosed()) {
                try {
                    connectionSocket.close();
                } catch (IOException ex) {
                    logger.error(ex);
                }
                System.out.println("Server: Client disconnected.");
                logger.info("Server: Client disconnected.");
            }
        }
    }
}
