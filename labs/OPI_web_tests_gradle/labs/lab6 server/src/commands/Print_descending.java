package commands;

import models.StudyGroup;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Vector;
import java.util.stream.Collectors;

public class Print_descending extends Command<String[]> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Console console;
    private CollectionManager collectionManager;
    private Integer client;
    public Print_descending(Console console, CollectionManager collectionManager, Integer client) {
        super("print_descending", "Сортирует элементы в порядке убывания");
        this.console = console;
        this.collectionManager = collectionManager;
        this.client = client;
    }
    @Override
    public Integer getClient() {
        return client;
    }
    @Override
//    public Useless execution(String[] command){
//        collectionManager.getCollection().sort(Collections.reverseOrder());
//        String s = "Коллекция по убыванию: ";
//        for (StudyGroup i: collectionManager.getCollection()){
//            s = s + "\n" + i + "\n";
//        }
//        return new Useless(s);
//    }
    public Useless execution(String[] command, Integer userId) {

        Vector<StudyGroup> sortedCollection = collectionManager.getCollection().stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toCollection(Vector::new));
        collectionManager.setCollection(sortedCollection);
        String s = sortedCollection.stream()
                .map(StudyGroup::toString)
                .collect(Collectors.joining("\n"));

        return new Useless("Коллекция по убыванию: \n" + s);
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
