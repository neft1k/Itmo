package commands;

import models.StudyGroup;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;

public class Clear extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public Clear(String name, String description) {
        super("clear", "очистить коллекцию");
    }

}
