package commands;

import models.StudyGroup;
import utility.Answer;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;

public class Save extends Command<String[]> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Console console;
    private CollectionManager collectionManager;

    public Save(Console console, CollectionManager collectionManager) {
        super("save", "сохраняет коллекцию в файл");
        this.console = console;
        this.collectionManager = collectionManager;
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

    @Override
    public Useless execution(String[] strings, Integer id) {
        return null;
    }

    public String execution(){
        System.out.println(collectionManager.getCollection());
        collectionManager.saveCollection();
        return ("Коллекция сохранилась в файле");

    }



}
