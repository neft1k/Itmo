package commands;

import models.StudyGroup;
import utility.CollectionManager;
import utility.Console;
import utility.StandartConsole;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Vector;

public class Info extends Command<String[]> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Console console;
    private CollectionManager collectionManager;
    private Integer client;

    public Info(Console console, CollectionManager collectionManager, Integer client) {
        super("info", "выводит информацию о коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
        this.client = client;
    }


    @Override
    public Useless execution(String[] command, Integer client) {
        LocalDateTime lastInitTime = collectionManager.getLastInitTime();
        LocalDateTime lastSaveTime = collectionManager.getLastSaveTime();
        Integer size = collectionManager.getCollection().size();
        String type = collectionManager.getCollection().getClass().toString();

        return new Useless("Информация о коллекции" + "\n" +
                "Тип коллекции: " + type + "\n" +
                "Размер коллекции:" + size + "\n" +
                "Последнее время инициализации: " + lastInitTime + "\n" +
                "Последнее время сохранения: " + lastSaveTime);
    }


    public Integer getClient() {
        return client;
    }

    @Override
    public String execution() {
        return null;
    }

    @Override
    public Useless execution(String[] command, String user) {
        return null;
    }


    @Override
    public Useless execution(String[] strings, StudyGroup studyGroup, Integer id) {
        return null;
    }

    @Override
    public Useless execution(String[] strings, StudyGroup studyGroup, Integer id, Integer userId) {
        return null;
    }

    @Override
    public Useless execution(String[] strings, Integer id, Integer userId) {
        return null;
    }
}
