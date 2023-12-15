package bichel.yauhen.core.data;

import bichel.yauhen.core.model.Hotel;
import bichel.yauhen.core.model.Review;

import java.util.List;
import java.util.Optional;

/**
 * Interface methods for readonly access for hotels and their reviews
 */
public interface ThreadSafeHotelReviewDataReadonly {
    /**
     * Gets information about the hotel with the given id
     * @param hotelId String value
     * @return Optional of Hotel
     */
    Optional<Hotel> findHotel(String hotelId);
    /**
     * Finds reviews using hotel id and return first amount items
     * @param hotelId int
     * @param amount int
     * @return list of reviews
     */
    List<Review> findReviews(int hotelId, int amount);
    /**
     * Finds reviews with required word and return first amount items
     * @param word String
     * @param amount int
     * @return list of reviews
     */
    List<Review> findReviewsByWord(String word, int amount);
}
