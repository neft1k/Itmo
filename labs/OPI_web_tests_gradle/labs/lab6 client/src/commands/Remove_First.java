package commands;

import models.StudyGroup;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;

public class Remove_First extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public Remove_First(String name, String description) {
        super("remove_first", "удалить первый элемент коллекции");
    }


}
