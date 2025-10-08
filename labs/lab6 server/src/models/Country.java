package models;

import java.io.Serial;
import java.io.Serializable;

public enum Country implements Serializable {

    UNITED_KINGDOM,
    GERMANY,
    CHINA,
    JAPAN;
    @Serial
    private static final long serialVersionUID = 1L;
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (var colors : values()) {
            nameList.append(colors.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}