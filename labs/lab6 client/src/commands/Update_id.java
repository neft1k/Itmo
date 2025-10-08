package commands;

import models.StudyGroup;
import utility.Answer;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;

public class Update_id extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private StudyGroup studyGroup;
    private Integer id;
    public Update_id(String name, Integer id, StudyGroup studyGroup) {
        super("update id", "обновить значение элемента коллекции , id которого равен данному");
        this.studyGroup = studyGroup;
        this.id = id;
    }

}
