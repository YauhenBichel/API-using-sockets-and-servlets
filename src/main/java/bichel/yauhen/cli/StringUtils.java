package bichel.yauhen.cli;

/**
 * Contains utility methods for String
 */
public final class StringUtils {
    /**
     * Validates whether string is integer
     * @param strNum string of integer
     * @return true if string can be parsed as integer, otherwise false
     */
    public static boolean isInteger(String strNum) {
        if (strNum == null) {
            return false;
        }

        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }
}
