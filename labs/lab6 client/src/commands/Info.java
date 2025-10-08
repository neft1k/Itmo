package commands;

import utility.CollectionManager;
import utility.Console;
import utility.StandartConsole;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Info extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer userId; // Поле для хранения userId

    public Info(String name, String description) {
        super("info", "выводит информацию о коллекции");
        this.userId = userId;
    }
    public Integer getUserId() {
        return userId;
    }

}
