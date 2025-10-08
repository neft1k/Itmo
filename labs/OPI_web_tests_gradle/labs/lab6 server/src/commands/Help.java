package commands;

import models.StudyGroup;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Help extends Command<String[]> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Console console;
    private CollectionManager collectionManager;
    private Integer client;
    public Help(Console console, CollectionManager collectionManager, Integer client) {
        super("help", "вывести справку по доступным командам");
        this.console = console;
        this.collectionManager = collectionManager;
        this.client = client;
    }
    @Override
    public Integer getClient() {
        return client;
    }
    @Override
    public Useless execution(String[] command, Integer client){
        return new Useless("help: вывести справку по доступным командам \n" +
                "add {element}: добавить новый элемент в коллекцию \n" +
                "average_of_students_count: cреднее значение количсетва учеников \n" +
                "clear: очистить коллекцию \n" +
                "info: выводит информацию о коллекции \n" +
                "print_descending: Сортирует элементы в порядке убывания \n" +
                "print_field_descending_group_admin: Выводит значение поля GroupAdmin в порядке убывания \n" +
                "remove_by_id: удалить элемент по id \n" +
                "remove_first: удалить первый элемент коллекции \n" +
                "remove_lower: удалить все элементы меньшие данного \n" +
                "save: сохраняет коллекцию в файл \n" +
                "show: все элементы коллекции в строковом представлении \n" +
                "shuffle: перемешать элементы коллекции в случайном порядке \n" +
                "update id: обновить значение элемента коллекции , id которого равен данному \n" +
                "execute_script: делает полный ужас, который мешает нормально жить \n" +
                "registration: регистрирует пользователя \n" +
                "login: авторизация пользователя");
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
