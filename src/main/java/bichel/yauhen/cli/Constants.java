package bichel.yauhen.cli;

/**
 * Constants for CLI interface for user collaboration
 */
public final class Constants {
    private Constants() {}

    public static final String CLI_EXIT_KEY = "q";
    public static final String CLI_COMMANDS_QUERY = "\nCommands:\nfind <hotelId> Example: find 26760\nfindReviews <hotelId> Example: findReviews 26760\nfindWord <word> Example: findWord hotel";
    public static final String CLI_NOTE_EXIT = "---\nPlease put symbol 'q' and press Enter to EXIT\n---";
    public static final String CLI_ERROR_NOT_VALID_KEY_MESSAGE = "---\nThe query is not valid. Please check the query\n---";
}
