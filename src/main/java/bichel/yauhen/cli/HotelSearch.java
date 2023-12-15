package bichel.yauhen.cli;

import bichel.yauhen.cli.enumeration.CliPathQueryKeyEnum;
import bichel.yauhen.core.data.ThreadSafeHotelReviewData;
import bichel.yauhen.core.processor.ReviewsProcessor;
import bichel.yauhen.core.processor.HotelsProcessor;

import java.util.HashMap;
import java.util.Map;

/** The driver class.
 * The main function should take the following command line arguments:
 * -hotels hotelFile -reviews reviewsDirectory -threads numThreads -output filepath
 * (order may be different)
 * and read general information about the hotels from the hotelFile (a JSON file),
 * as well as concurrently read hotel reviews from the json files in reviewsDirectory.
 * The data should be loaded into data structures that allow efficient search.
 * If the -output flag is provided, hotel information (about all hotels and reviews)
 * should be output into the given file.
 */
public class HotelSearch {
    /**
        cli arguments:
     -hotels hotelFile -reviews reviewsDirectory -threads numThreads
     -hotels input/hotels/hotels.json -reviews input/reviews
     **/
    public static void main(String[] args) {
        Map<CliPathQueryKeyEnum, String> keyValuePathMap = new HashMap<>();
        for (int i = 0; i < args.length - 1; i += 2) {
            keyValuePathMap.put(CliPathQueryKeyEnum.enumByValue(args[i]), args[i + 1]);
        }

        CliRequestHandler cliRequestHandler = new CliRequestHandler(
                new HotelsProcessor(),
                new ReviewsProcessor(),
                new ThreadSafeHotelReviewData(),
                keyValuePathMap);
        cliRequestHandler.run();
    }
}
