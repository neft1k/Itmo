package commands;

import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Help extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer userId; // Поле для хранения userId

    public Help(String name, String description, Integer userId)  {
        super("help", "вывести справку по доступным командам");
        this.userId = userId;

    }

    public Integer getUserId() {
        return userId;
    }
}
