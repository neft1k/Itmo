package commands;

import models.StudyGroup;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Show extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer userId; // Поле для хранения userId
    public Show(String name, String description) {
        super("show", "все элементы коллекции в строковом представлении");
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }
}
