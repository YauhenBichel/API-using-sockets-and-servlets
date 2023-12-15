package bichel.yauhen.core.data;

import bichel.yauhen.core.comparator.ReviewComparatorByDate;
import bichel.yauhen.core.comparator.ReviewComparatorByDateAndReviewId;
import bichel.yauhen.core.model.Hotel;
import bichel.yauhen.core.model.Review;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static bichel.yauhen.core.Constants.WORD_PATTERN;

/**
 * Provides parsed data from hotel and review files
 */
public class HotelReviewData implements ThreadSafeHotelReviewDataReadonly {

    private static final Logger logger = LogManager.getLogger(HotelReviewData.class);

    //Map<HotelId, Hotel>
    private final Map<String, Hotel> hotelMap;
    //Map<ReviewId, Review>
    public Map<String, Review> reviewMap;
    //Map<HotelId, List<Review>>
    private final Map<Integer, List<Review>> hotelIdReviewMap;
    //Map<Word, TreeMap<Frequency, List<Review>>>
    private final Map<String, SortedMap<Integer,List<Review>>> wordToFreqToReviewsMap;
    private final Map<String, List<Review>> wordReviewMap;

    public HotelReviewData() {
        hotelMap = new TreeMap<>();
        reviewMap = new HashMap<>();

        hotelIdReviewMap = new HashMap<>();
        wordReviewMap = new HashMap<>();
        wordToFreqToReviewsMap = new HashMap<>();
    }

    /**
     * Adds parsed hotel for searching
     * @param hotel Hotel
     */
    public void addHotel(Hotel hotel) {
        hotelMap.put(String.valueOf(hotel.getId()), hotel);
        hotelIdReviewMap.put(hotel.getId(), new ArrayList<>());
    }

    /**
     * Adds parsed review for searching
     * @param review Review
     */
    public void addReview(Review review) {
        reviewMap.put(review.getId(), review);
    }

    /**
     * Maps reviews to hotels
     */
    public void mapHotelReview() {
        for(Map.Entry<String, Review> entry: reviewMap.entrySet()) {
            Review review  = entry.getValue();

            if (hotelIdReviewMap.containsKey(review.getHotelId())) {
                List<Review> reviews = hotelIdReviewMap.get(review.getHotelId());
                reviews.add(review);
                hotelIdReviewMap.put(review.getHotelId(), reviews);
            }
        }
    }

    /**
     * Maps reviews to word
     */
    public void mapWordReview() {
        for(Map.Entry<String, Review> entry: reviewMap.entrySet()) {
            Review review  = entry.getValue();

            String[] words = review.getReviewText().split(WORD_PATTERN);
            for (String word : words) {
                word = word.toLowerCase();
                if(!wordReviewMap.containsKey(word)) {
                    wordReviewMap.put(word, new ArrayList<>());
                }

                List<Review> reviewsForWord = wordReviewMap.get(word);
                reviewsForWord.add(review);
                wordReviewMap.put(word, reviewsForWord);
            }
        }
    }

    /**
     * Creates inverted index for searching words
     */
    public void createInvertedIndexMap() {
        mapHotelReview();
        mapWordReview();
        updateWordToFreqToReviewsMap();
    }

    /**
     * Gets information about the hotel with the given id
     * @param hotelId String value
     * @return Optional of Hotel
     */
    public Optional<Hotel> findHotel(String hotelId) {
        logger.info("Find hotel by hotelId {}", hotelId);
        Hotel hotel = hotelMap.get(hotelId);

        return hotel == null ? Optional.empty() : Optional.of(hotel);
    }

    /**
     * Finds reviews using hotel id and return first amount items
     * @param hotelId int
     * @param amount int
     * @return list of reviews
     */
    @Override
    public List<Review> findReviews(int hotelId, int amount) {
        return findReviews(hotelId).stream().limit(amount).collect(Collectors.toList());
    }

    /**
     * Finds reviews with required word and return first amount items
     * @param word String
     * @param amount int
     * @return list of reviews
     */
    @Override
    public List<Review> findReviewsByWord(String word, int amount) {
        logger.info("Reviews with word {}", word);
        List<Review> reviews = wordReviewMap.getOrDefault(word, new ArrayList<>())
                .stream().limit(amount).toList();
        return reviews;
    }

    /**
     * Iterator for Hotels
     * @return Iterator
     */
    public Iterator<Hotel> getHotelIterator() {
        return hotelMap.values().iterator();
    }

    /**
     * Returns all reviews for the hotel with the given hotel id.
     * Reviews should be sorted by date (most recent first), and if the dates are the same, by
     * review id.
     * @param hotelId int number
     * @return all reviews for the hotel with the given hotel id
     */
    public List<Review> findReviews(int hotelId) {
        logger.info("Reviews by hotel Id: {}", hotelId);
        List<Review> reviews = hotelIdReviewMap.getOrDefault(hotelId, new ArrayList<>());
        reviews.sort(new ReviewComparatorByDateAndReviewId());

        return reviews;
    }

    /**
     * Returns all reviews that contain a given word. Reviews
     * should be sorted by word frequency and then by date (most recent first)
     * @param word String
     * @return all reviews that contain a given word
     */
    public SortedMap<Integer, List<Review>> findWord(String word) {
        logger.info("Reviews by word:");
        SortedMap<Integer, List<Review>> freqReviewsMap = wordToFreqToReviewsMap.getOrDefault(word, new TreeMap<>());
        if(freqReviewsMap.isEmpty()) {
            logger.info("word is not found");
        }
        return freqReviewsMap;
    }

    /**
     * Merges data
     * @param hotelReviewData ThreadSafeHotelReviewData
     */
    public synchronized void merge(ThreadSafeHotelReviewData hotelReviewData) {
        this.reviewMap =
                Stream.of(this.reviewMap, hotelReviewData.reviewMap)
                        .flatMap(map -> map.entrySet().stream())
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (first, second) -> first));
    }

    private void updateWordToFreqToReviewsMap() {
        for (Map.Entry<Integer, List<Review>> entry : hotelIdReviewMap.entrySet()) {
            Map<String, Integer> wordFreqMap = new HashMap<>();

            List<Review> reviews = entry.getValue();
            for (Review review : reviews) {
                String[] words = review.getReviewText().split(WORD_PATTERN);
                for (String word : words) {
                    wordFreqMap.put(word.toLowerCase(), wordFreqMap.getOrDefault(word, 0) + 1);
                }
            }

            SortedMap<Integer, List<Review>> freqMap = new TreeMap<>(Comparator.reverseOrder());

            for (Review review : reviews) {
                String[] words = review.getReviewText().split(WORD_PATTERN);
                for (String word : words) {
                    word = word.toLowerCase();
                    int freq = wordFreqMap.get(word);
                    if (!freqMap.containsKey(freq)) {
                        freqMap.put(freq, new ArrayList<>());
                    }

                    List<Review> freqWords = freqMap.get(freq);
                    freqWords.add(review);
                    freqWords.sort(new ReviewComparatorByDate());
                    freqMap.put(freq, freqWords);
                    wordToFreqToReviewsMap.put(word, freqMap);
                }
            }
        }
    }
}
