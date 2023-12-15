package bichel.yauhen.web.socket.server;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Implements the http server (that processes GET requests) using raw sockets.
 * RawSocketServer should be general and not contain anything related to hotels/reviews.
 * */
public class RawSocketServer {

    private static final Logger logger = LogManager.getLogger(RawSocketServer.class);

    private static final int PORT = 8080;

    private final ExecutorService executorService;
    private final Map<String, String> handlers;
    private final Map<String, Pair<Class, Object>> resources;
    private boolean alive;

    public RawSocketServer() {
        executorService = Executors.newCachedThreadPool();
        alive = true;
        handlers = new HashMap<>();
        resources = new HashMap<>();
    }

    /**
     * Maps a given URL path/endpoint to the name of the class that will handle requests coming at this endpoint
     *
     * @param path      end point
     * @param className name of the handler class
     */
    public void addHandler(String path, String className) {
        handlers.put(path, className);
    }

    /**
     * Maps a give URL path and a pair of a resource and its type
     * @param path end point
     * @param resourceType Class
     * @param resource Object
     */
    public void addResource(String path, Class resourceType, Object resource) {
        resources.put(path, new ImmutablePair<>(resourceType, resource));
    }

    /**
     * Starts the raw socket server
     */
    public void start() {
        ServerSocket welcomingSocket = null;
        Socket connectionSocket = null;
        try {
            welcomingSocket = new ServerSocket(PORT);
            while (alive) {
                logger.info("Server: Waiting for connection...");
                connectionSocket = welcomingSocket.accept();
                logger.info("Server: Client connected.");

                Stream<String> linesStream = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()))
                        .lines();

                List<String> lines = linesStream.limit(4).collect(Collectors.toList());

                HttpRequest httpRequest = new HttpRequest();
                try {
                    httpRequest.parse(lines);
                } catch (Exception ex) {
                    logger.error(ex);
                    continue;
                }

                HttpResponse httpResponse = new HttpResponse(new PrintWriter(new OutputStreamWriter(connectionSocket.getOutputStream()), true));

                if(!httpRequest.getType().equals("GET")) {
                    logger.error("Not allowed type: " + httpRequest.getType());
                    httpResponse.sendResponse("405 Method not allowed", "405");
                    continue;
                }

                String httpHandlerName = handlers.get(httpRequest.getPath());
                if(httpHandlerName == null) {
                    logger.error("Invalid endpoint: " + httpHandlerName);
                    httpResponse.sendResponse("invalid endpoint", "404");
                    continue;
                }

                RequestWorker worker = new RequestWorker(
                        httpHandlerName,
                        resources.get(httpRequest.getPath()).getRight(),
                        resources.get(httpRequest.getPath()).getLeft(),
                        httpRequest,
                        httpResponse,
                        connectionSocket);

                executorService.submit(worker);
            }
        } catch (Exception ex) {
            logger.error("Exception occurred while using the socket", ex);
        } finally {
            try {
                if (welcomingSocket != null && !welcomingSocket.isClosed()) {
                    welcomingSocket.close();
                }
                if (connectionSocket != null && !connectionSocket.isClosed()) {
                    connectionSocket.close();
                }
                executorService.shutdown();
                executorService.awaitTermination(1, TimeUnit.SECONDS);
            } catch (IOException ex) {
                logger.error("Could not close the socket", ex);
            } catch (InterruptedException ex) {
                logger.error(ex);
            }
        }
    }
}
