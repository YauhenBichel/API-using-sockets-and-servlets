package bichel.yauhen.web;

import bichel.yauhen.cli.enumeration.CliPathQueryKeyEnum;
import bichel.yauhen.core.data.ThreadSafeHotelReviewData;
import bichel.yauhen.core.data.ThreadSafeHotelReviewDataReadonly;
import bichel.yauhen.core.processor.ReviewsProcessor;
import bichel.yauhen.web.app.servlet.WordServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bichel.yauhen.web.app.HotelReviewsLoader;
import bichel.yauhen.core.processor.HotelsProcessor;
import bichel.yauhen.web.app.servlet.HotelServlet;
import bichel.yauhen.web.app.servlet.ReviewsServlet;
import bichel.yauhen.web.app.servlet.WeatherServlet;
import bichel.yauhen.web.jetty.server.JettyServer;

import java.util.HashMap;
import java.util.Map;

/** Driver class for running the Jetty server.
 * Create a jar file from this class.  */
public class JettyServerDriver {
    private static final Logger logger = LogManager.getLogger(JettyServerDriver.class);

    public static void main(String[] args) {

        //-hotels input/hotels/hotels.json -reviews input/reviews -threads 3
        Map<CliPathQueryKeyEnum, String> keyValuePathMap = new HashMap<>();
        for (int i = 0; i < args.length - 1; i += 2) {
            keyValuePathMap.put(CliPathQueryKeyEnum.enumByValue(args[i]), args[i + 1]);
        }

        HotelReviewsLoader dataLoader = new HotelReviewsLoader(new HotelsProcessor(),
                new ReviewsProcessor(),
                new ThreadSafeHotelReviewData(),
                keyValuePathMap);

        ThreadSafeHotelReviewDataReadonly hotelReviewData = dataLoader.load();

        JettyServer server = new JettyServer();
        server.addHandler("/index", WordServlet.class, ThreadSafeHotelReviewDataReadonly.class, hotelReviewData);
        server.addHandler("/reviews", ReviewsServlet.class, ThreadSafeHotelReviewDataReadonly.class, hotelReviewData);
        server.addHandler("/hotelInfo", HotelServlet.class, ThreadSafeHotelReviewDataReadonly.class, hotelReviewData);
        server.addHandler("/weather", WeatherServlet.class, ThreadSafeHotelReviewDataReadonly.class, hotelReviewData);

        try {
            server.start();
        } catch (Exception ex) {
            logger.error(ex);
        }
    }
}
