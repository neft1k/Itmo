package commands;

import models.StudyGroup;
import utility.Answer;
import utility.CollectionManager;
import utility.Console;
import utility.StandartConsole;

import java.io.Serial;
import java.io.Serializable;
import java.util.Vector;
import java.util.stream.Collectors;

public class Remove_by_id extends Command<StudyGroup> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Console console;
    private CollectionManager collectionManager;
    private  Integer id;
    private Integer client;
    public Remove_by_id(Console console, CollectionManager collectionManager, Integer id, Integer client) {
        super("remove_by_id", "удалить элемент по id");
        this.console = console;
        this.collectionManager = collectionManager;
        this.id = id;
        this.client = client;
    }
    @Override
    public Integer getClient() {
        return client;
    }

    @Override
    public Integer getId() {
        return id;
    }

    //    public Useless execution(String[] command, Integer id){
//        if(collectionManager.getCollection().remove(collectionManager.byId(id))){
//            return new Useless("Элемент с id " + id + " удален");
//        }else{
//            return new Useless("Не такого элемента");
//        }
//    }
    public Useless execution(String[] command, Integer id, Integer userId) {
//        if(collectionManager.getCollection().remove(collectionManager.byId(id))){
//            return new Useless("Элемент с id " + id + " удален");
//        }else{
//            return new Useless("Не такого элемента");
//        }
        Vector<StudyGroup> newCollection = collectionManager.getCollection().stream()
                .filter(el -> el != collectionManager.byId(id) || !el.getUserId().equals(userId))
                .collect(Collectors.toCollection(Vector::new));
        if (newCollection.size() < collectionManager.getCollection().size()) {
            collectionManager.setCollection(newCollection);
        }
        StudyGroupDAO studyGroupDAO = new StudyGroupDAO();
        if (studyGroupDAO.deleteStudyGroupById(id, userId)){
//            collectionManager.getCollection().remove(collectionManager.byId(id));
            collectionManager.setCollection(newCollection);
            return new Useless("Элемент с id " + id + " удален");
        }else {
            return new Useless("Не такого элемента");
        }
    }


    @Override
    public Useless execution(StudyGroup studyGroup, Integer userId) throws Answer.AnswerBreak {
        return null;
    }

    @Override
    public Useless execution(String[] command, String user) {
        return null;
    }

    @Override
    public Useless execution(String[] command, Integer userId) {
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
    public String execution() {
        return null;
    }
}
