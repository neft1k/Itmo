package commands;

import models.StudyGroup;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Clear extends Command<String[]> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Console console;
    private CollectionManager collectionManager;
    private Integer client;
    public Clear(Console console, CollectionManager collectionManager, Integer client) {
        super("clear", "очистить коллекцию");
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
//        Vector<StudyGroup> emptyVector = Stream.<StudyGroup>empty().collect(Collectors.toCollection(Vector::new));
//        collectionManager.setCollection(emptyVector);
        StudyGroupDAO studyGroupDAO = new StudyGroupDAO();
        studyGroupDAO.clearStudyGroupById(userId);
        collectionManager.setCollection(studyGroupDAO.getStudyGroupsByUserId());
//        Vector<StudyGroup> collection = studyGroupDAO.getStudyGroupsByUserId(Login.getUserId());
//        collection.clear();

        return new Useless("Коллекция очистилась");
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
