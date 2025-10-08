package commands;

import models.StudyGroup;
import utility.Answer;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;
import java.util.Vector;
import java.util.stream.Collectors;

public class Remove_lower extends Command<String[]> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Console console;
    private CollectionManager collectionManager;
    private Integer client;
    public Remove_lower(Console console, CollectionManager collectionManager, Integer client) {
        super("remove_lower", "удалить все элементы меньшие данного");
        this.console = console;
        this.collectionManager = collectionManager;
        this.client = client;
    }
    @Override
    public Integer getClient() {
        return client;
    }
    @Override
    public Useless execution(String[] command, Integer userId) {
        StudyGroupDAO studyGroupDAO = new StudyGroupDAO();
        collectionManager.setCollection(studyGroupDAO.getStudyGroupsByUserId());
        int size = collectionManager.getCollection().size();
//
//        Vector<StudyGroup> emptyVector = collectionManager.getCollection().stream()
//                .filter(el -> false)
//                .collect(Collectors.toCollection(Vector::new));
//
//        collectionManager.setCollection(emptyVector);

        studyGroupDAO.clearStudyGroupById(userId);
        return new Useless("Удалено " + size + " элементов коллекции");
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
