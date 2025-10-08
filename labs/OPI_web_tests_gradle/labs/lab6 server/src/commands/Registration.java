package commands;

import models.StudyGroup;
import utility.Answer;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;

public class Registration extends Command<String[]> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Console console;
    private CollectionManager collectionManager;
    private String user;

    public Registration(Console console, CollectionManager collectionManager, String user) {
        super("registration", "регистрация");
        this.console = console;
        this.collectionManager = collectionManager;
        this.user = user;

    }
    public String getUser(){
        return user;
    }



    @Override
    public Useless execution(String[] command, String user) {
        String[] userCommand = user.split(" ");
        String username = userCommand[0];
        String password = userCommand[1];
//        return new Useless(UserHandler.registerUser(username, password));
        String message = String.valueOf(UserHandler.registerUser(username, password));

        // В процессе регистрации мы не можем получить userId напрямую, т.к. ваш метод возвращает только строку
        // Нужно изменить метод UserHandler.registerUser чтобы он возвращал результат с userId
        return new Useless(message); // Предполагаем, что при регистрации userId неизвестен и возвращаем -1
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

    @Override
    public Useless execution(String[] strings, Integer id) {
        return null;
    }

    @Override
    public String execution() {
        return null;
    }


}


