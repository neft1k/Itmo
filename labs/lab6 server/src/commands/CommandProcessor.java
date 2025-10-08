
package commands;
//import models.StudyGroup;
//import utility.Answer;
//import utility.CollectionManager;
//import utility.Console;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class CommandProcessor {
//    private Map<String, Command> commandMap;
//    private Integer commandId;
//    private String[] command;
//    private Info info;
//    private Update_id update_id;
//    private Clear clear;
//    private Average_of_students_count averageOfStudentsCount;
//    private Help help;
//    private Shuffle shuffle;
//    private Show show;
//    private Remove_First removeFirst;
//    private Print_descending print_descending;
//    private Save save;
//    private Print_field_descending_group_admin printFieldDescendingGroupAdmin;
//    private Add add;
//    private StudyGroup studyGroup;
//    private Remove_by_id removeById;
//    private Integer id;
//    private Remove_lower remove_lower;
//    private Registration registration;
//    private Login login;
//    private String user;
//    public CommandProcessor(Console console, CollectionManager collectionManager) {
//        commandMap = new HashMap<>();
//        this.login = new Login(console, user);
//        this.registration = new Registration(console, collectionManager, user);
//        this.info = new Info(console, collectionManager);
//        this.update_id = new Update_id(console, collectionManager, id, studyGroup);
//        this.clear = new Clear(console, collectionManager);
//        this.averageOfStudentsCount = new Average_of_students_count(console, collectionManager);
//        this.help = new Help(console, collectionManager);
//        this.shuffle = new Shuffle(console, collectionManager);
//        this.show = new Show(console, collectionManager);
//        this.removeFirst = new Remove_First(console, collectionManager);
//        this.print_descending = new Print_descending(console, collectionManager);
//        this.save = new Save(console, collectionManager);
//        this.removeById = new Remove_by_id(console, collectionManager, id);
//        this.remove_lower = new Remove_lower(console, collectionManager);
//        try {
//            this.add = new Add(console,collectionManager, studyGroup);
//        } catch (Answer.AnswerBreak e) {
//        }
//        this.printFieldDescendingGroupAdmin =new Print_field_descending_group_admin(console, collectionManager);
//        commandMap.put("login", this.login);
//        commandMap.put("registration", this.registration);
//        commandMap.put("add", this.add);
//        commandMap.put("info", this.info);
//        commandMap.put("show", this.show);
//        commandMap.put("clear", this.clear);
//        commandMap.put("shuffle", this.shuffle);
//        commandMap.put("remove_first", this.removeFirst);
//        commandMap.put("print_descending", this.print_descending);
//        commandMap.put("remove_lower", this.remove_lower);
//        commandMap.put("remove_by_id", this.removeById);
//        commandMap.put("average_of_students_count", this.averageOfStudentsCount);
//        commandMap.put("update", this.update_id);
//        commandMap.put("save", this.save);
//        commandMap.put("print_field_descending_group_admin", this.printFieldDescendingGroupAdmin);
//        commandMap.put("help", this.help);
//    }
//
//    public Useless processCommand(String[] command) {
//
//
//        command[1] = command[1].trim();
//        command[2] = command[2].trim();
//        Command currentCommand = this.commandMap.get(command[0]);
//        if ("save".equals(command[0])){
//
//            return new Useless("Сработал save");
//        }else {
//            if (currentCommand == null){
//                return new Useless("Данной команды не существует!");
//            }else{
//                try {
//                    return currentCommand.execution(command);
//                } catch (Answer.AnswerBreak e) {
//                    return new Useless("");
//                }
//            }
//        }
//
//    }
//    public Useless processCommand(String[] command, String user) {
//
//        this.user = user;
//
//        command[1] = command[1].trim();
//        command[2] = command[2].trim();
//        Command currentCommand = this.commandMap.get(command[0]);
//        if ("save".equals(command[0])){
//
//            return new Useless("Сработал save");
//        }else {
//            if (currentCommand == null){
//                return new Useless("Данной команды не существует!");
//            }else{
//                return currentCommand.execution(command, user);
//            }
//        }
//
//    }
//    public Useless processCommand(String[] command, StudyGroup studyGroup) {
//        this.studyGroup = studyGroup;
//        command[1] = command[1].trim();
//        command[2] = command[2].trim();
//        Command currentCommand = this.commandMap.get(command[0]);
//        if (currentCommand == null){
//            return new Useless("Данной команды не существует!");
//        }else{
//            return currentCommand.execution(command, studyGroup);
//        }
//    }
//    public Useless processCommand(String[] command, Integer id) {
//        this.id = id;
//        command[1] = command[1].trim();
//        command[2] = command[2].trim();
//        Command currentCommand =  this.commandMap.get(command[0]);
//        if (currentCommand == null){
//            return new Useless("Данной команды не существует!");
//        }else{
//            return currentCommand.execution(command, id);
//        }
//    }
//    public Useless processCommand(String[] command, Integer id, StudyGroup studyGroup) {
//        this.id = id;
//        this.studyGroup = studyGroup;
//        command[1] = command[1].trim();
//        command[2] = command[2].trim();
//        Command currentCommand =  this.commandMap.get(command[0]);
//        if (currentCommand == null){
//            return new Useless("Данной команды не существует!");
//        }else{
//            return currentCommand.execution(command, id, studyGroup);
//        }
//    }
//
//
//}
import models.StudyGroup;
import utility.Answer;
import utility.CollectionManager;
import utility.Console;

import java.util.HashMap;
import java.util.Map;

public class CommandProcessor {
    private Map<String, Command> commandMap;
    private Integer commandId;
    private String[] command;
    private Info info;
    private Update_id update_id;
    private Clear clear;
    private Average_of_students_count averageOfStudentsCount;
    private Help help;
    private Shuffle shuffle;
    private Show show;
    private Remove_First removeFirst;
    private Print_descending print_descending;
    private Save save;
    private Print_field_descending_group_admin printFieldDescendingGroupAdmin;
    private Add add;
    private StudyGroup studyGroup;
    private Remove_by_id removeById;
    private Integer id;
    private Remove_lower remove_lower;
    private Registration registration;
    private Login login;
    private String user;
    private Integer userId;

    public CommandProcessor(Console console, CollectionManager collectionManager) {
        commandMap = new HashMap<>();
        this.login = new Login(console, user);
        this.registration = new Registration(console, collectionManager, user);
        this.info = new Info(console, collectionManager, userId);
        this.update_id = new Update_id(console, collectionManager, id, studyGroup, userId);
        this.clear = new Clear(console, collectionManager, userId);
        this.averageOfStudentsCount = new Average_of_students_count(console, collectionManager, userId);
        this.help = new Help(console, collectionManager, userId);
        this.shuffle = new Shuffle(console, collectionManager, userId);
        this.show = new Show(console, collectionManager, userId);
        this.removeFirst = new Remove_First(console, collectionManager, userId);
        this.print_descending = new Print_descending(console, collectionManager, userId);
//        this.save = new Save(console, collectionManager);
        this.removeById = new Remove_by_id(console, collectionManager, id, userId);
        this.remove_lower = new Remove_lower(console, collectionManager, userId);
        try {
            this.add = new Add(console, collectionManager, studyGroup,userId);
        } catch (Answer.AnswerBreak e) {
        }
        this.printFieldDescendingGroupAdmin = new Print_field_descending_group_admin(console, collectionManager, userId);
        commandMap.put("login", this.login);
        commandMap.put("registration", this.registration);
        commandMap.put("add", this.add);
        commandMap.put("info", this.info);
        commandMap.put("show", this.show);
        commandMap.put("clear", this.clear);
        commandMap.put("shuffle", this.shuffle);
        commandMap.put("remove_first", this.removeFirst);
        commandMap.put("print_descending", this.print_descending);
        commandMap.put("remove_lower", this.remove_lower);
        commandMap.put("remove_by_id", this.removeById);
        commandMap.put("average_of_students_count", this.averageOfStudentsCount);
        commandMap.put("update", this.update_id);
        commandMap.put("save", this.save);
        commandMap.put("print_field_descending_group_admin", this.printFieldDescendingGroupAdmin);
        commandMap.put("help", this.help);
    }

    public Useless processCommand(String[] command, Integer userId) {
        command[1] = command[1].trim();
        command[2] = command[2].trim();
        Command currentCommand = this.commandMap.get(command[0]);
        if ("save".equals(command[0])) {
            return new Useless("Сработал save");
        } else {
            if (currentCommand == null) {
                return new Useless("Данной команды не существует!");
            } else {
                return currentCommand.execution(command, userId);
            }
        }
    }

    public Useless processCommand(String[] command, String user) {
        this.user = user;
        command[1] = command[1].trim();
        command[2] = command[2].trim();
        Command currentCommand = this.commandMap.get(command[0]);
        if ("save".equals(command[0])) {
            return new Useless("Сработал save");
        } else {
            if (currentCommand == null) {
                return new Useless("Данной команды не существует!");
            } else {
                return currentCommand.execution(command, user);
            }
        }
    }

    public Useless processCommand(String[] command, StudyGroup studyGroup, Integer userId) {
        this.studyGroup = studyGroup;
        command[1] = command[1].trim();
        command[2] = command[2].trim();
        Command currentCommand = this.commandMap.get(command[0]);
        if (currentCommand == null) {
            return new Useless("Данной команды не существует!");
        } else {
            return currentCommand.execution(command, studyGroup, userId);
        }
    }

    public Useless processCommand(String[] command, Integer id, Integer userId) {
        this.id = id;
        this.userId = id;
        command[1] = command[1].trim();
        command[2] = command[2].trim();
        Command currentCommand = this.commandMap.get(command[0]);
        if (currentCommand == null) {
            return new Useless("Данной команды не существует!");
        } else {
            return currentCommand.execution(command, id, userId);
        }
    }

    public Useless processCommand(String[] command, Integer id, StudyGroup studyGroup, Integer userId) {
        this.id = id;
        this.studyGroup = studyGroup;
        System.out.println(command[0]);
        command[1] = command[1].trim();
        command[2] = command[2].trim();

        Command currentCommand = this.commandMap.get(command[0]);
        if (currentCommand == null) {
            return new Useless("Данной команды не существует!");
        } else {
            return currentCommand.execution(command, studyGroup, id, userId);
        }
    }
}