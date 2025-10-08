package commands;

import models.StudyGroup;
import utility.Answer;
import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;

public class Login extends Command<String[]> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Console console;
    private CollectionManager collectionManager;
    private String user;
    private static int userId = -1;

    public Login(Console console, String user) {
        super("login", "авторизация");
        this.console = console;
        this.collectionManager = collectionManager;
        this.user = user;

    }
    public String getUser(){
        return user;
    }
    public static int getUserId() {
        return userId;
    }


    @Override
    public Useless execution(String[] command, String user) {
        String[] userCommand = user.split(" ");
        String username = userCommand[0];

        String password = userCommand[1];

        UserHandler.AuthenticationResult result = UserHandler.authenticateUser(username, password);

        userId = result.getUserId();  // Получаем userId из результата аутентификации
        String strUserId = String.valueOf(userId);;

        return new Useless(result.getMessage() + " , " + strUserId);

//        UserHandler.AuthenticationResult result = UserHandler.authenticateUser(username, password);
//
//        userId = result.getUserId();
//        System.out.println(userId);
//        return new Useless(result.getMessage());
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
