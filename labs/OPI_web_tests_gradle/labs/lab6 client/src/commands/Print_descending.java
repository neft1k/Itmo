package commands;

import models.StudyGroup;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;

public class Print_descending extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public Print_descending(String name, String description) {
        super("print_descending", "Сортирует элементы в порядке убывания");
    }

}
