package bichel.yauhen.web.app;

import bichel.yauhen.cli.enumeration.CliPathQueryKeyEnum;
import bichel.yauhen.core.data.ThreadSafeHotelReviewData;
import bichel.yauhen.core.processor.ReviewsProcessor;
import bichel.yauhen.core.model.Hotel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bichel.yauhen.core.processor.HotelsProcessor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hotels and reviews loader for project 4 restful service
 * The data is threadsafe
 */
public record HotelReviewsLoader(HotelsProcessor hotelsProcessor, ReviewsProcessor reviewsProcessor,
                                 ThreadSafeHotelReviewData hotelReviewData,
                                 Map<CliPathQueryKeyEnum, String> keyValuePathMap) {

    private static final Logger logger = LogManager.getLogger(HotelReviewsLoader.class);

    /**
     * Loads data
     * @return ThreadSafeHotelReviewData
     */
    public ThreadSafeHotelReviewData load() {
        logger.info("Data loading: started");
        loadHotelsAndTheirReviews(keyValuePathMap);
        logger.info("Data loading: map hotel review");
        hotelReviewData.mapHotelReview();
        logger.info("Data loading: map word review");
        hotelReviewData.mapWordReview();
        logger.info("Data loading: done");
        return hotelReviewData;
    }

    /**
     * Processes hotel and review files
     * @param keyValuePathMap Map<CliPathQueryKeyEnum, String> contains cli params and their values
     */
    private void loadHotelsAndTheirReviews(Map<CliPathQueryKeyEnum, String> keyValuePathMap) {
        String hotelFile = keyValuePathMap.get(CliPathQueryKeyEnum.HOTELS);

        logger.info("Hotels loading: started");
        List<Hotel> hotels = hotelsProcessor.parseHotels(hotelFile);
        for (Hotel hotel : hotels) {
            hotelReviewData.addHotel(hotel);
        }
        logger.info("Hotels loading: done");

        int threads = Integer.parseInt(keyValuePathMap.getOrDefault(CliPathQueryKeyEnum.THREADS, "1"));
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        logger.info("Threads number: " + threads);

        if (keyValuePathMap.containsKey(CliPathQueryKeyEnum.REVIEWS)) {
            logger.info("Reviews loading: started");
            String reviewDir = keyValuePathMap.get(CliPathQueryKeyEnum.REVIEWS);
            reviewsProcessor.parseReviews(reviewDir, executor, hotelReviewData);
            logger.info("Reviews loading: done");
        }
    }
}
