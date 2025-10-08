package commands;

import models.StudyGroup;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Vector;
import java.util.stream.Collectors;

public class Show extends Command<String[]> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Console console;
    private CollectionManager collectionManager;
    private Integer client;
    public Show(Console console, CollectionManager collectionManager, Integer client) {
        super("show", "все элементы коллекции в строковом представлении");
        this.console = console;
        this.collectionManager = collectionManager;
        this.client = client;
    }
    @Override
    public Integer getClient() {
        return client;
    }
    @Override
    public Useless execution(String[] command, Integer client) {
//        StudyGroupDAO studyGroupDAO = new StudyGroupDAO();
//        System.out.println(Login.getUserId());
//        collectionManager.setCollection(studyGroupDAO.getStudyGroupsByUserId(Login.getUserId()));
//        System.out.println(collectionManager.getCollection());
//        Vector<StudyGroup> collection = studyGroupDAO.getStudyGroupsByUserId(Login.getUserId());
//        collection = studyGroupDAO.getStudyGroupsByUserId(Login.getUserId());
        if (collectionManager.getCollection().isEmpty()) {
            return new Useless("В коллекции пусто");
        } else {
            String result = collectionManager.getCollection().stream()
                    .map(StudyGroup::toString)
                    .collect(Collectors.joining("\n"));
            return new Useless(result);
        }
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
