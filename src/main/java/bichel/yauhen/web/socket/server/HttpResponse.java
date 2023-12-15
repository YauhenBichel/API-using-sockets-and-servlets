package bichel.yauhen.web.socket.server;

import java.io.PrintWriter;

/**
 * Response component for sending response message
 */
public class HttpResponse {
    private final PrintWriter writer;

    public HttpResponse(PrintWriter writer) {
        this.writer = writer;
    }

    /**
     * Sends response to client
     * @param response String
     * @param status String of HTTP status
     */
    public void sendResponse(String response, String status) {
        writer.println("HTTP/1.1 " + status);
        writer.println("Content-Type: application/json; charset=UTF-8");
        writer.println("Content-Length: " + response.length());
        writer.println();
        writer.println(response);
        writer.flush();
        writer.close();
    }
}
