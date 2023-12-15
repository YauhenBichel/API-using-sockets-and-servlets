package bichel.yauhen.cli.strategy;

import bichel.yauhen.cli.enumeration.CliPathQueryKeyEnum;
import bichel.yauhen.core.data.HotelReviewData;
import bichel.yauhen.core.comparator.ReviewComparatorByDateAndReviewId;
import bichel.yauhen.core.model.Hotel;
import bichel.yauhen.core.model.Review;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Strategy for printing results of data handling in files
 */
public class FileResultsOutputStrategy implements ResultsOutputStrategy {

    private static final Logger logger = LogManager.getLogger(FileResultsOutputStrategy.class);
    private final Map<CliPathQueryKeyEnum, String> keyValuePathMap;
    private final HotelReviewData hotelReviewData;

    public FileResultsOutputStrategy(Map<CliPathQueryKeyEnum, String> keyValuePathMap,
                                     HotelReviewData hotelReviewData) {
        this.keyValuePathMap = keyValuePathMap;
        this.hotelReviewData = hotelReviewData;
    }

    /**
     * Writes data into output files
     */
    @Override
    public void process() {

        hotelReviewData.mapHotelReview();

        final String filepath = keyValuePathMap.get(CliPathQueryKeyEnum.OUTPUT);

        try (BufferedWriter out = new BufferedWriter(new FileWriter(filepath))) {
            Iterator<Hotel> hotelIterator = hotelReviewData.getHotelIterator();
            while (hotelIterator.hasNext()) {
                Hotel hotel = hotelIterator.next();
                out.write(hotel.toString());
                if (keyValuePathMap.containsKey(CliPathQueryKeyEnum.REVIEWS)) {
                    List<Review> reviews = hotelReviewData.findReviews(hotel.getId());
                    reviews.sort(new ReviewComparatorByDateAndReviewId());
                    StringBuffer sb = new StringBuffer();
                    for (Review review : reviews) {
                        sb.append(review.toString());
                    }
                    out.write(sb.toString());
                }
                out.write("\n");
            }
        } catch (IOException ex) {
            logger.error(ex);
        }
    }
}
