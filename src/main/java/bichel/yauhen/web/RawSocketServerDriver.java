package bichel.yauhen.web;

import bichel.yauhen.web.socket.server.RawSocketServer;
import bichel.yauhen.cli.enumeration.CliPathQueryKeyEnum;
import bichel.yauhen.core.data.ThreadSafeHotelReviewDataReadonly;
import bichel.yauhen.core.data.ThreadSafeHotelReviewData;
import bichel.yauhen.core.processor.HotelsProcessor;
import bichel.yauhen.core.processor.ReviewsProcessor;
import bichel.yauhen.web.app.socket.handler.HotelHandler;
import bichel.yauhen.web.app.socket.handler.ReviewsHandler;
import bichel.yauhen.web.app.socket.handler.WeatherHandler;
import bichel.yauhen.web.app.socket.handler.WordHandler;
import bichel.yauhen.web.app.HotelReviewsLoader;

import java.util.HashMap;
import java.util.Map;

/** Driver class for running the RawSocketServer.
 * Create a jar file from this class. */
public class RawSocketServerDriver {
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

        RawSocketServer socketServer = new RawSocketServer();
        socketServer.addHandler("/index", WordHandler.class.getName());
        socketServer.addHandler("/reviews", ReviewsHandler.class.getName());
        socketServer.addHandler("/hotelInfo", HotelHandler.class.getName());
        socketServer.addHandler("/weather", WeatherHandler.class.getName());

        socketServer.addResource("/index", ThreadSafeHotelReviewDataReadonly.class, hotelReviewData);
        socketServer.addResource("/reviews", ThreadSafeHotelReviewDataReadonly.class, hotelReviewData);
        socketServer.addResource("/hotelInfo", ThreadSafeHotelReviewDataReadonly.class, hotelReviewData);
        socketServer.addResource("/weather", ThreadSafeHotelReviewDataReadonly.class, hotelReviewData);

        socketServer.start();
    }
}
