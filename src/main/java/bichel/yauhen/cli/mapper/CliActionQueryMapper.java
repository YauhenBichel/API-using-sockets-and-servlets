package bichel.yauhen.cli.mapper;

import bichel.yauhen.cli.Constants;
import bichel.yauhen.cli.enumeration.CliActionCommandEnum;
import bichel.yauhen.cli.exception.IncorrectCliQueryException;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper query of actions to map of CliActionCommandEnum and parameter
 */
public class CliActionQueryMapper {
    public Map<CliActionCommandEnum, String> mapActionQueryToPairs(String query) {
        Map<CliActionCommandEnum, String> keyValueMap = new HashMap<>();
        String[] words = query.split(" ");

        if (words[0].equals(CliActionCommandEnum.FIND_BY_HOTEL_ID.getValue())) {
            keyValueMap.put(CliActionCommandEnum.FIND_BY_HOTEL_ID, words[1]);
        } else if (words[0].equals(CliActionCommandEnum.FIND_REVIEWS_BY_HOTEL_ID.getValue())) {
            keyValueMap.put(CliActionCommandEnum.FIND_REVIEWS_BY_HOTEL_ID, words[1]);
        } else if (words[0].equals(CliActionCommandEnum.FIND_WORD.getValue())) {
            keyValueMap.put(CliActionCommandEnum.FIND_WORD, words[1]);
        } else if (!words[0].isEmpty()) {
            final String errMsg = Constants.CLI_ERROR_NOT_VALID_KEY_MESSAGE + "\nNot valid key is " + words[0];
            throw new IncorrectCliQueryException(errMsg);
        } else if (!(words.length <= 1 || words[1].trim().isEmpty())) {
            final String errMsg = "\nNo parameter for command is " + words[1];
            throw new IncorrectCliQueryException(errMsg);
        }

        validateKeyValuePairs(keyValueMap);

        return keyValueMap;
    }

    private void validateKeyValuePairs(Map<CliActionCommandEnum, String> keyValueMap) {
        if (keyValueMap.isEmpty()) {
            final String errMsg = Constants.CLI_ERROR_NOT_VALID_KEY_MESSAGE + "\nRequired one of the commands: 'find <hotelId>' or 'findReviews <hotelId>' or 'findWord <word>'";
            throw new IncorrectCliQueryException(errMsg);
        }
    }
}
