package commands;

import models.StudyGroup;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;

public class Print_field_descending_group_admin extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public Print_field_descending_group_admin(String name, String description) {
        super("print_field_descending_group_admin", "Выводит значение поля GroupAdmin в порядке убывания");
    }

}
