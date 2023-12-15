package bichel.yauhen.cli;

import bichel.yauhen.cli.enumeration.CliPathQueryKeyEnum;
import bichel.yauhen.core.data.ThreadSafeHotelReviewData;
import bichel.yauhen.core.processor.ReviewsProcessor;
import bichel.yauhen.cli.strategy.CliResultsOutputStrategy;
import bichel.yauhen.cli.strategy.FileResultsOutputStrategy;
import bichel.yauhen.cli.strategy.ResultsOutputStrategy;
import bichel.yauhen.core.model.Hotel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bichel.yauhen.core.processor.HotelsProcessor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class for command line interface
*/
public class CliRequestHandler {

    private static final Logger logger = LogManager.getLogger(CliRequestHandler.class);

    private final ThreadSafeHotelReviewData hotelReviewData;
    private final HotelsProcessor hotelsProcessor;
    private final ReviewsProcessor reviewsProcessor;
    private final Map<CliPathQueryKeyEnum, String> keyValuePathMap;

    public CliRequestHandler(HotelsProcessor hotelsProcessor,
                             ReviewsProcessor reviewsProcessor,
                             ThreadSafeHotelReviewData hotelReviewData,
                             Map<CliPathQueryKeyEnum, String> keyValuePathMap) {
        this.hotelReviewData = hotelReviewData;
        this.hotelsProcessor = hotelsProcessor;
        this.reviewsProcessor = reviewsProcessor;
        this.keyValuePathMap = keyValuePathMap;
    }

    /**
     * Starts the app
     * The user should be able to type find,
     * findReviews and findWord in the console and get relevant results.
     * The data should NOT be sorted during the search.
     */
    public void run() {
        loadHotelsAndTheirReviews(keyValuePathMap);

        ResultsOutputStrategy outputStrategy;
        if(keyValuePathMap.containsKey(CliPathQueryKeyEnum.OUTPUT)) {
            logger.info("Print results to files");
            outputStrategy = new FileResultsOutputStrategy(keyValuePathMap, hotelReviewData);
        } else {
            hotelReviewData.createInvertedIndexMap();
            logger.info("Print results to console");
            outputStrategy = new CliResultsOutputStrategy(hotelReviewData);
        }

        outputStrategy.process();
    }

    private void loadHotelsAndTheirReviews(Map<CliPathQueryKeyEnum, String> keyValuePathMap) {
        String hotelFile = keyValuePathMap.get(CliPathQueryKeyEnum.HOTELS);

        List<Hotel> hotels = hotelsProcessor.parseHotels(hotelFile);
        for (Hotel hotel : hotels) {
            hotelReviewData.addHotel(hotel);
        }

        int threads = Integer.parseInt(keyValuePathMap.getOrDefault(CliPathQueryKeyEnum.THREADS, "1"));
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        if(keyValuePathMap.containsKey(CliPathQueryKeyEnum.REVIEWS)) {
            String reviewDir = keyValuePathMap.get(CliPathQueryKeyEnum.REVIEWS);
            reviewsProcessor.parseReviews(reviewDir, executor, hotelReviewData);
        }
    }
}
