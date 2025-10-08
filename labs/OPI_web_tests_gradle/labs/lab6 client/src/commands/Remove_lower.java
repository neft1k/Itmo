package commands;

import models.StudyGroup;
import utility.Answer;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;

public class Remove_lower extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public Remove_lower(String name, String description) {
        super("remove_lower", "удалить все элементы меньшие данного");
    }



}
