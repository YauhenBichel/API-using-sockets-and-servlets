package bichel.yauhen.core.data;

import bichel.yauhen.core.model.Hotel;
import bichel.yauhen.core.model.Review;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Provides parsed data from hotel and review files
 */
public final class ThreadSafeHotelReviewData extends HotelReviewData {
    private static final Logger logger = LogManager.getLogger(ThreadSafeHotelReviewData.class);

    private final ReentrantReadWriteLock lock;

    public ThreadSafeHotelReviewData() {
        lock = new ReentrantReadWriteLock();
    }

    /**
     * Adds review to collection
     * @param review Review
     */
    public void addReview(Review review) {
        try {
            lock.writeLock().lock();
            super.addReview(review);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Gets information about the hotel with the given id
     * @param hotelId
     * @return Optional of Hotel
     */
    public Optional<Hotel> findHotel(String hotelId) {
        try {
            lock.readLock().lock();
            logger.debug("find hotel with id {}", hotelId);
            return super.findHotel(hotelId);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Gets list of reviews for hotel id and the limited amount
     * @param hotelId int
     * @param amount int
     * @return list of reviews
     */
    @Override
    public List<Review> findReviews(int hotelId, int amount) {
        try {
            lock.readLock().lock();
            logger.debug("find reviews of hotel with id {}", hotelId);
            List<Review> reviews = super.findReviews(hotelId).stream().limit(amount).collect(Collectors.toList());
            return reviews;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Finds reviews with required word and return first amount items
     * @param word String
     * @param amount int
     * @return list of reviews
     */
    @Override
    public List<Review> findReviewsByWord(String word, int amount) {
        try {
            lock.readLock().lock();
            logger.debug("find reviews with word {}", word);
            return super.findReviewsByWord(word, amount);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Iterator of hotels
     * @return Iterator of Hotel
     */
    public Iterator<Hotel> getHotelIterator() {
        return super.getHotelIterator();
    }

    /**
     * Returns all reviews for the hotel with the given hotel id.
     * Reviews should be sorted by date (most recent first), and if the dates are the same, by
     * review id.
     * @param hotelId
     * @return all reviews for the hotel with the given hotel id
     */
    public List<Review> findReviews(int hotelId) {
        try {
            lock.readLock().lock();
            logger.debug("find reviews for hotel with id {}", hotelId);
            return super.findReviews(hotelId);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Returns all reviews that contain a given word. Reviews
     * should be sorted by word frequency and then by date (most recent first)
     * @param word
     * @return all reviews that contain a given word
     */
    public SortedMap<Integer, List<Review>> findWord(String word) {
        try {
            lock.readLock().lock();
            logger.debug("find word {}", word);
            return super.findWord(word);
        } finally {
            lock.readLock().unlock();
        }
    }
}
