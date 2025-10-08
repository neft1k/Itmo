package models;

import java.io.Serial;
import java.io.Serializable;

public enum Semester implements Serializable {
    THIRD,
    FOURTH,
    FIFTH,
    SIXTH;
    @Serial
    private static final long serialVersionUID = 1L;
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (var semester : values()) {
            nameList.append(semester.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}