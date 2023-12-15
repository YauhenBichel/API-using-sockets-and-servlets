package bichel.yauhen.cli.enumeration;

/**
 * Enumeration of cli commands
 */
public enum CliActionCommandEnum {
    FIND_BY_HOTEL_ID("find"),
    FIND_REVIEWS_BY_HOTEL_ID("findReviews"),
    FIND_WORD("findWord");

    private String value;

    CliActionCommandEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
