package commands;

import models.StudyGroup;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;

public class Average_of_students_count extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer userId; // Поле для хранения userId
    public Average_of_students_count(String name, String description, Integer userId) {
        super("average_of_students_count", "вывести значения поля groupAdmin в порядке убывания");
        this.userId = userId;
    }
    public Integer getUserId() {
        return userId;
    }
}
