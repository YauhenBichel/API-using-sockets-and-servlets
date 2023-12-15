package bichel.yauhen.web.socket.server;

/** Contains a method to process http request from the client.  */
public interface HttpHandler {

    /**
     * Handles http get request from the client
     * @param request client's http request
     * @param response http response
     */
    void processRequest(HttpRequest request, HttpResponse response);
}
