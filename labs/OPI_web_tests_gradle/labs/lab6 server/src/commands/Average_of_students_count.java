package commands;

import models.StudyGroup;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Vector;

public class Average_of_students_count extends Command<String[]> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Console console;
    private CollectionManager collectionManager;
    private Integer client;
    public Average_of_students_count(Console console, CollectionManager collectionManager, Integer client) {
        super("average_of_students_count", "вывести значения поля groupAdmin в порядке убывания");
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


        if(collectionManager.getCollection().isEmpty()){
            return new Useless("В коллекции пусто");
        }
        double avg = collectionManager.getCollection().stream()
                .mapToLong(StudyGroup::getStudentsCount) // преобразует поток StudyGroup в поток int
                .average() // вычисляет среднее значение потока. Результатом является OptionalDouble.
                .orElse(0.0); // извлекает значение из OptionalDouble, если оно есть, иначе возвращает 0.

        return new Useless("Среднее значение количсетва учеников:" + avg);
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
