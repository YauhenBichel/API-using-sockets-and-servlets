package bichel.yauhen.core.comparator;

import bichel.yauhen.core.model.Review;

import java.util.Comparator;

/**
 * Review comparator by date
 */
public class ReviewComparatorByDate implements Comparator<Review> {
    @Override
    public int compare(Review review1, Review review2) {
        return review1.getReviewSubmissionTime().compareTo(review2.getReviewSubmissionTime());
    }
}
