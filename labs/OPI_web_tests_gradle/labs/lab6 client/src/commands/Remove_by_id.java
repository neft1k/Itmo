package commands;

import models.StudyGroup;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;

public class Remove_by_id extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private StudyGroup studyGroup;
    private Integer id;
    public Remove_by_id(String name, String description, Integer id) {
        super("remove_by_id", "удалить элемент по id");
        this.id = id;
    }
    public Integer getId(){
        return id;
    }
}
