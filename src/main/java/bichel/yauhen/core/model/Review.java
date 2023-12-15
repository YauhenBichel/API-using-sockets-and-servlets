package bichel.yauhen.core.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Review model for json file with reviews
 */
public final class Review {
    @SerializedName(value = "reviewId")
    private String id;
    @SerializedName(value = "hotelId")
    private Integer hotelId;
    @SerializedName(value = "userNickname")
    private String userNickname;
    @SerializedName(value = "reviewText")
    private String reviewText;
    @SerializedName(value = "title")
    private String title;
    @SerializedName(value = "ratingOverall")
    private String ratingOverall;

    public Review(String reviewId, Integer hotelId, String userNickname, String reviewText,
                  String title, String ratingOverall,
                  String reviewSubmissionTime) {
        this.id = reviewId;
        this.hotelId = hotelId;
        this.userNickname = userNickname.isBlank() ? "Anonymous" : userNickname;
        this.reviewText = reviewText;
        this.title = title;
        this.ratingOverall = ratingOverall;
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        this.date = LocalDate.parse(reviewSubmissionTime, formatter).toString();
    }

    @SerializedName(value = "reviewSubmissionTime")
    private String date;

    public String getId() {
        return id;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public String getReviewText() {
        return reviewText;
    }

    public String getTitle() {
        return title;
    }

    public String getRatingOverall() {
        return ratingOverall;
    }

    public LocalDate getReviewSubmissionTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDate.parse(date, formatter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review)) return false;

        Review review = (Review) o;

        if (id != null ? !id.equals(review.id) : review.id != null) return false;
        if (hotelId != null ? !hotelId.equals(review.hotelId) : review.hotelId != null) return false;
        if (userNickname != null ? !userNickname.equals(review.userNickname) : review.userNickname != null)
            return false;
        if (reviewText != null ? !reviewText.equals(review.reviewText) : review.reviewText != null) return false;
        if (title != null ? !title.equals(review.title) : review.title != null) return false;
        if (ratingOverall != null ? !ratingOverall.equals(review.ratingOverall) : review.ratingOverall != null)
            return false;
        return date != null ? date.equals(review.date) : review.date == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (hotelId != null ? hotelId.hashCode() : 0);
        result = 31 * result + (userNickname != null ? userNickname.hashCode() : 0);
        result = 31 * result + (reviewText != null ? reviewText.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (ratingOverall != null ? ratingOverall.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {

        String nickname = userNickname.isBlank() ? "Anonymous" : userNickname;

        final StringBuffer sb = new StringBuffer();
        sb.append("\n--------------------");
        sb.append("\nReview by ").append(nickname).append(" on ").append(getReviewSubmissionTime());
        sb.append("\nRating: ").append(ratingOverall);
        sb.append("\nReviewId: ").append(id);
        sb.append("\n").append(title);
        sb.append("\n").append(reviewText);
        return sb.toString();
    }
}
