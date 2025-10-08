package commands;

import models.StudyGroup;
import utility.Answer;
import utility.CollectionManager;
import utility.Console;
import utility.StandartConsole;

import java.io.Serial;
import java.io.Serializable;

public  class Add extends Command<StudyGroup> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Console console;
    private CollectionManager collectionManager;
    private StudyGroup studyGroup;
    private Integer client;

    public Add(Console console, CollectionManager collectionManager,StudyGroup studyGroup, Integer client) throws Answer.AnswerBreak {
        super("add", "добавить новый элемент в коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
        this.studyGroup = studyGroup;
        this.client = client;
    }
    public Add() {
        super(null, null);
    }

    public Useless execution(String[] command, StudyGroup studyGroup, Integer client) {
        StudyGroupDAO studyGroupDAO = new StudyGroupDAO();
        studyGroupDAO.saveStudyGroup(studyGroup, client);
        collectionManager.add(studyGroupDAO.getObjectByUserId());
        return new Useless("Группа успешно добавлена ");
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
    public Integer getClient() {
        return client;
    }

    @Override
    public StudyGroup getStudyGroup() {
        return studyGroup;
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
    public Useless execution(String[] strings, Integer id) {
        return null;
    }

    @Override
    public String execution() {
        return null;
    }
}
