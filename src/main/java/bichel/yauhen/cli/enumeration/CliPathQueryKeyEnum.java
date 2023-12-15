package bichel.yauhen.cli.enumeration;

/**
 * Enumeration of cli key for paths
 */
public enum CliPathQueryKeyEnum {
    HOTELS("-hotels"),
    REVIEWS("-reviews"),
    THREADS("-threads"),
    OUTPUT("-output");

    private String value;

    CliPathQueryKeyEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static CliPathQueryKeyEnum enumByValue(String value) {
        for (CliPathQueryKeyEnum key : CliPathQueryKeyEnum.values()) {
            if (key.getValue().equals(value)) {
                return key;
            }
        }
        throw new RuntimeException("Unable to find key with value " + value);
    }
}
