package bichel.yauhen.web.socket.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** A class that represents http request */
public class HttpRequest {
    private static final Logger logger = LogManager.getLogger(HttpRequest.class);

    //type of the request "GET"
    private String type;
    private String path;
    //version of the protocol
    private String version;
    //query parameters of the request in a map
    private Map<String, String> queryParams;

    /**
     * Parses the first line of request and generates HttpRequest
     * @param text http request headers
     * @return HttpRequest
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    public HttpRequest parse(List<String> text) throws MalformedURLException, UnsupportedEncodingException {

        String[] lines = text.get(0).split(System.lineSeparator());
        if(lines.length > 0) {
            String[] props = lines[0].split(" ");

            type = props[0];
            version = props[2];
            URL url = null;
            try {
                String host = "";
                for(String line: text) {
                    if(line.contains("Host")) {
                        host = line.split("Host: ")[1];
                        break;
                    }
                }

                url = new URL("http://" + host + props[1]);
            } catch (MalformedURLException ex) {
                logger.error(ex);
                throw ex;
            }

            path = url.getPath();

            if(url.getQuery() != null && !url.getQuery().isEmpty()) {
                try {
                    queryParams = splitQuery(url.getQuery());
                } catch (UnsupportedEncodingException ex) {
                    logger.error(ex);
                    throw ex;
                }
            }
        }

        return this;
    }

    private Map<String, String> splitQuery(String urlQuery) throws UnsupportedEncodingException {
        Map<String, String> queryPairs = new HashMap<>();
        String[] pairs = urlQuery.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if(idx == -1) {
                break;
            }
            queryPairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return queryPairs;
    }

    public String getType() {
        return type;
    }
    public String getPath() {
        return path;
    }
    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HttpRequest)) return false;

        HttpRequest that = (HttpRequest) o;

        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;
        return queryParams != null ? queryParams.equals(that.queryParams) : that.queryParams == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (queryParams != null ? queryParams.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("HttpRequest{");
        sb.append("type='").append(type).append('\'');
        sb.append(", path='").append(path).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", queryParams=").append(queryParams);
        sb.append('}');
        return sb.toString();
    }
}
