package commands;

import models.StudyGroup;
import utility.CollectionManager;
import utility.Console;

import java.io.Serializable;
import java.util.Collections;
import java.util.stream.Collectors;

public class Shuffle extends Command<String[]> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Console console;
    private CollectionManager collectionManager;
    private Integer client;
    public Shuffle(Console console, CollectionManager collectionManager, Integer client) {
        super("shuffle", "перемешать элементы коллекции в случайном порядке");
        this.console = console;
        this.collectionManager = collectionManager;
        this.client = client;
    }
    @Override
    public Integer getClient() {
        return client;
    }
    @Override
    public Useless execution(String[] command, Integer userId){
        Collections.shuffle(collectionManager.getCollection());
        String result = collectionManager.getCollection().stream()
                .map(StudyGroup::toString)
                .collect(Collectors.joining("\n"));
        return new Useless("элементы коллекции перемешаны в случайном порядке \n" + result);

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
