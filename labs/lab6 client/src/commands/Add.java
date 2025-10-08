package commands;

import models.StudyGroup;
import utility.Answer;
import utility.CollectionManager;
import utility.Console;
import utility.StandartConsole;

import java.io.Serial;
import java.io.Serializable;

public class Add extends Command  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private StandartConsole console;
    private CollectionManager collectionManager;
    private StudyGroup studyGroup;
    private Integer userId; // Поле для хранения userId
    public Add(String name, String description, StudyGroup studyGroup) throws Answer.AnswerBreak {
        super("add", "добавить новый элемент в коллекцию");
        this.studyGroup = studyGroup;
        this.userId = userId;
    }
    public StudyGroup getStudyGroup(){
        return studyGroup;
    }

    public Integer getUserId() {
        return userId;
    }
}
