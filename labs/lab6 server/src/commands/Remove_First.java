package commands;

import models.StudyGroup;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Vector;
import java.util.stream.Collectors;

public class Remove_First extends Command<String[]> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Console console;
    private CollectionManager collectionManager;
    private Integer client;
    public Remove_First(Console console, CollectionManager collectionManager, Integer client) {
        super("remove_first", "удалить первый элемент коллекции");
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
        if (studyGroupDAO.getStudyGroupsByUserId().isEmpty()){
            return new Useless("В коллекции ничего нет");
        } else {

            Vector<StudyGroup> collection = collectionManager.getCollection();


            StudyGroup minIdElement = collection.stream()
                    .filter(el -> el.getUserId().equals(userId))
                    .min(Comparator.comparingInt(StudyGroup::getId))
                    .orElse(null);

            if (minIdElement == null) {
                return new Useless("Такого элемента не существует или он не принадлежит пользователю с id " + userId);
            }


            Vector<StudyGroup> newCollection = collection.stream()
                    .filter(el -> !el.equals(minIdElement))
                    .collect(Collectors.toCollection(Vector::new));


            collectionManager.setCollection(newCollection);
            studyGroupDAO.deleteStudyGroupWithMinId(userId);
            return new Useless("первый элемент удален");
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
