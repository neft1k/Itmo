package commands;

import models.StudyGroup;
import utility.Answer;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Vector;
import java.util.stream.Collectors;

public class Update_id extends Command<String[]> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Console console;
    private CollectionManager collectionManager;
    private StudyGroup studyGroup;
    private  Integer id;
    private Integer client;
    public Update_id(Console console, CollectionManager collectionManager, Integer id, StudyGroup studyGroup, Integer client) {
        super("update id", "обновить значение элемента коллекции , id которого равен данному");
        this.console = console;
        this.collectionManager = collectionManager;
        this.studyGroup = studyGroup;
        this.id = id;
        this.client = client;
    }
    @Override
    public Integer getClient() {
        return client;
    }

    @Override
    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    @Override
    public Integer getId() {
        return id;
    }

    //    public Useless execution(String[] command, Integer id, StudyGroup studyGroup) {
//        try {
//            studyGroup.setId(id);
//            collectionManager.getCollection().set(collectionManager.getCollection().indexOf(collectionManager.byId(id)), studyGroup);
//            return new Useless("Обновился значение элемента коллекции id которого равен данному");
//        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e){
//            return new Useless("Вводите нормальное id (число)");
//        }
//    }
    public Useless execution(String[] command, StudyGroup studyGroup, Integer id, Integer userId) {

//        System.out.println("2121212121");
        StudyGroupDAO studyGroupDAO = new StudyGroupDAO();
        if (studyGroupDAO.deleteStudyGroupById(id, userId)){
            Vector<StudyGroup> newCollection = collectionManager.getCollection().stream()
                    .filter(el -> el != collectionManager.byId(id) || !el.getUserId().equals(userId))
                    .collect(Collectors.toCollection(Vector::new));
            if (newCollection.size() < collectionManager.getCollection().size()) {
                collectionManager.setCollection(newCollection);
            };
//            collectionManager.getCollection().remove(collectionManager.byId(id));
            collectionManager.setCollection(newCollection);
            studyGroupDAO.saveStudyGroup(studyGroup, userId);
            collectionManager.add(studyGroupDAO.getObjectByUserId());
            return new Useless("Элемент с id " + id + " удален и добавлен новый");
        }else {
            return new Useless("Не такого элемента");
        }




//        System.out.println(1);
//        StudyGroup oldStudyGroup = collectionManager.getCollection().stream()
//                .filter(el -> el == collectionManager.byId(id) && el.getUserId().equals(userId))
//                .findAny()
//                .orElse(null);
//        System.out.println(2);
//        if (oldStudyGroup == null) {
//            return new Useless("Вводите нормальное id (число) или элемент не принадлежит пользователю с id " + userId);
//        }
//
//        // Удаляем найденный элемент из коллекции
//        Vector<StudyGroup> newCollection = collectionManager.getCollection().stream()
//                .filter(el -> !el.equals(oldStudyGroup))
//                .collect(Collectors.toCollection(Vector::new));
//
//        // Обновляем коллекцию в менеджере
//        collectionManager.setCollection(newCollection);
//        StudyGroupDAO studyGroupDAO = new StudyGroupDAO();
//
//        if (studyGroupDAO.deleteStudyGroupById(id, userId)){
//            studyGroupDAO.saveStudyGroup(studyGroup, userId);
//            collectionManager.add(studyGroupDAO.getObjectByUserId());
//            return new Useless("Обновился значение элемента коллекции id которого равен данному");
//        }else {
//            return new Useless("Не такого элемента");
//        }
//        studyGroup.setId(id);
//        collectionManager.getCollection().set(collectionManager.getCollection().indexOf(oldStudyGroup), studyGroup);
//        return new Useless("Обновился значение элемента коллекции id которого равен данному");
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
    public Useless execution(String[] strings, Integer id, Integer userId) {
        return null;
    }

    @Override
    public Useless execution(String[] strings, Integer id) {
        return null;
    }

    @Override
    public String execution() {
        return null;
    }
}
