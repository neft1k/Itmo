



//import models.Response;
//import models.StudyGroup;
//import utility.Answer;
//import utility.CollectionManager;
//import utility.Console;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class CommandProcessor {
//    private Map<String, Runnable> commandMap;
//    private Integer commandId;
//    private String[] command;
//    private String username;
//    private String password;
//    private Boolean authenticated = false;
//    public CommandProcessor(Console console, CollectionManager collectionManager) {
//
//        commandMap = new HashMap<>();
//
//        commandMap.put("add", () -> {
//            try {
//                StudyGroup a = Answer.ansStudyGroup(console, collectionManager.getFreeId());
//                Response response = new Sender("localhost",2324).sendObject(new Add("add", "", a));
//                System.out.println(response.getMessage());
//            } catch (IOException | Answer.AnswerBreak e) {
//                System.out.println("Нет такого хоста");
//            }
//        });
//        commandMap.put("info", () -> {
//            try {
//                Response response = new Sender("localhost",2324).sendObject(new Info("info", ""));
//                System.out.println(response.getMessage());
//            } catch (IOException e) {
//                System.out.println("Нет такого хоста");
//            }
//        });
//        commandMap.put("show",  () -> {
//            try {
//                Response response = new Sender("localhost",2324).sendObject(new Show("show", ""));
//                System.out.println(response.getMessage());
//            } catch (IOException e) {
//                System.out.println("Нет такого хоста");
//            }
//        });
//        commandMap.put("registration",  () -> {
//            try {
//                Response response = new Sender("localhost",2324).sendObject(new Registration("registration", "", username + " " + password));
//                System.out.println(response.getMessage());
//            } catch (IOException e) {
//                System.out.println("Нет такого хоста");
//            }
//        });
//        commandMap.put("login",  () -> {
//            try {
//                Response response = new Sender("localhost",2324).sendObject(new Login("login", "", username + " " + password));
//                if ((response.getMessage()).equals("Вы успешно вошли!")){
//                    this.authenticated = true;
//                }
//                System.out.println(response.getMessage());
//            } catch (IOException e) {
//                System.out.println("Нет такого хоста");
//            }
//        });
//        commandMap.put("clear",  () -> {
//            try {
//                Response response = new Sender("localhost",2324).sendObject(new Clear("clear", ""));
//                System.out.println(response.getMessage());
//            } catch (IOException e) {
//                System.out.println("Нет такого хоста");
//            }
//        });
//        commandMap.put("shuffle",  () -> {
//            try {
//                Response response = new Sender("localhost",2324).sendObject(new Shuffle("shuffle", ""));
//                System.out.println(response.getMessage());
//            } catch (IOException e) {
//                System.out.println("Нет такого хоста");
//            }
//        });
//        commandMap.put("remove_first",  () -> {
//            try {
//                Response response = new Sender("localhost",2324).sendObject(new Remove_First("remove_first", ""));
//                System.out.println(response.getMessage());
//            } catch (IOException e) {
//                System.out.println("Нет такого хоста");
//            }
//        });
//        commandMap.put("print_descending",  () -> {
//            try {
//                Response response = new Sender("localhost",2324).sendObject(new Print_descending("print_descending", ""));
//                System.out.println(response.getMessage());
//            } catch (IOException e) {
//                System.out.println("Нет такого хоста");
//            }
//        });
//        commandMap.put("remove_lower",  () -> {
//            try {
//                Response response = new Sender("localhost",2324).sendObject(new Remove_lower("remove_lower", ""));
//                System.out.println(response.getMessage());
//            } catch (IOException e) {
//                System.out.println("Нет такого хоста");
//            }
//        });
//        commandMap.put("remove_by_id", () -> {
//            try {
//                Response response = new Sender("localhost",2324).sendObject(new Remove_by_id("remove_by_id", "", commandId));
//                System.out.println(response.getMessage());
//            } catch (IOException e) {
//                System.out.println("Нет такого хоста");
//            }
//        });
//        commandMap.put("average_of_students_count",  () -> {
//            try {
//                Response response = new Sender("localhost",2324).sendObject(new Average_of_students_count("average_of_students_count", ""));
//                System.out.println(response.getMessage());
//            } catch (IOException e) {
//                System.out.println("Нет такого хоста");
//            }
//        });
//        commandMap.put("update",   () -> {
//            try {
//                StudyGroup b = Answer.ansStudyGroup(console, collectionManager.getFreeId());
//                Response response = new Sender("localhost",2324).sendObject(new Update_id("update", commandId, b));
//                System.out.println(response.getMessage());
//            } catch (IOException | Answer.AnswerBreak e) {
//                System.out.println("Нет такого хоста");
//            }
//        });
////        commandMap.put("save",  () -> {
////            try {
////                Response response = new Sender("localhost",2324).sendObject(new Show("show", ""));
////                System.out.println(response.getMessage());
////            } catch (IOException e) {
////                System.out.println("Нет такого хоста");
////            }
////        });
//        commandMap.put("print_field_descending_group_admin",  () -> {
//            try {
//                Response response = new Sender("localhost",2324).sendObject(new Print_field_descending_group_admin("print_field_descending_group_admin", ""));
//                System.out.println(response.getMessage());
//            } catch (IOException e) {
//                System.out.println("Нет такого хоста");
//            }
//        });
//        commandMap.put("help",  () -> {
//            try {
//                Response response = new Sender("localhost",2324).sendObject(new Help("help", ""));
//                System.out.println(response.getMessage());
//            } catch (IOException e) {
//                System.out.println("Нет такого хоста");
//            }
//        });
//    }
//
//    public Boolean processCommand(String[] command) {
//        this.username = command[2];
//        this.password = command[3];
//
//        try {
//            this.commandId = Integer.parseInt(command[1]);
//            Runnable action = commandMap.get(command[0]);
//            if (action != null) {
//                action.run();
//            } else {
//                if (!command[0].equals("execute_skript")){
//                    System.out.println("Неизвестная команда");
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Вводите нормальное id (число)");
//        }
//
//        return null;
//    }
//
//    public Boolean getAuthenticated() {
//        return authenticated;
//    }
//}
package commands;

import models.Response;
import models.StudyGroup;
import utility.Answer;
import utility.CollectionManager;
import utility.Console;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandProcessor {
    private Map<String, Runnable> commandMap;
    private Integer commandId;
    private String[] command;
    private String username;
    private String password;
    private Boolean authenticated = false;
    private Integer userId; // Новое поле для хранения userId

    public CommandProcessor(Console console, CollectionManager collectionManager) {
        commandMap = new HashMap<>();

        commandMap.put("add", () -> {
            try {
                StudyGroup a = Answer.ansStudyGroup(console, collectionManager.getFreeId());
                Response response = new Sender("localhost", 2324).sendObject(new Add("add", "", a), userId);
                if (response != null) {
                    System.out.println(response.getMessage());
                } else {
                    System.out.println("Получен пустой ответ от сервера");
                }
            } catch (IOException | Answer.AnswerBreak e) {
                System.out.println("Нет такого хоста");
            }
        });
        commandMap.put("info", () -> {
            try {
                Response response = new Sender("localhost", 2324).sendObject(new Info("info", ""), userId);
                System.out.println(response.getMessage());
            } catch (IOException e) {
                System.out.println("Нет такого хоста");
            }
        });
        commandMap.put("show", () -> {
            try {
                Response response = new Sender("localhost", 2324).sendObject(new Show("show", ""), userId);
                System.out.println(response.getMessage());
            } catch (IOException e) {
                System.out.println("Нет такого хоста");
            }
        });
        commandMap.put("registration", () -> {
            try {
                Response response = new Sender("localhost", 2324).sendObject(new Registration("registration", "", username + " " + password), userId);
                System.out.println(response.getMessage());
                if (response.getExitCode()) {
                    this.userId = response.getUserId(); // Предполагается, что Response содержит userId
                }
            } catch (IOException e) {
                System.out.println("Нет такого хоста");
            }
        });
        commandMap.put("login", () -> {
            try {
                Response response = new Sender("localhost", 2324).sendObject(new Login("login", "", username + " " + password), userId);
                if (response != null) {
                    System.out.println(response.getMessage());
                } else {
                    System.out.println("Получен пустой ответ от сервера");
                }
            } catch (IOException e) {
                System.out.println("Нет такого хоста");
            }
        });
//        commandMap.put("login", () -> {
//            try {
//                Response response = new Sender("localhost", 2324).sendObject(new Login("login", "", username + " " + password), userId);
//                if ((response.getMessage()).equals("Вы успешно вошли!")) {
//                    this.authenticated = true;
//                    this.userId = response.getUserId(); // Предполагается, что Response содержит userId
//                }
//                System.out.println(response.getMessage());
//            } catch (IOException e) {
//                System.out.println("Нет такого хоста");
//            }
//        });
        // Остальные команды аналогично, передавайте userId при создании объекта команды.
        // ...
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Response processCommand(String[] command) {
        this.username = command[2];
        this.password = command[3];

        try {
            this.commandId = Integer.parseInt(command[1]);
            Runnable action = commandMap.get(command[0]);
            if (action != null) {
                action.run();
            } else {
                if (!command[0].equals("execute_skript")) {
                    System.out.println("Неизвестная команда");
                }
            }
        } catch (Exception e) {
            System.out.println("Вводите нормальное id (число)");
        }

        return null;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }
}