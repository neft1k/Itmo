package models;

import commands.CommandProcessor;
import models.StudyGroup;
import utility.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

//public class Main {
//    public static void main(String[] args) {
//        Scanner userScanner = new Scanner(System.in);
//
//        var console = new StandartConsole();
//
//
//        String inputString = "lab6file.txt";
//        DumpManager dumpManager = new DumpManager(inputString, console);
//        var collectionManager = new CollectionManager(dumpManager);
//        CommandProcessor processor = new CommandProcessor(console, collectionManager);
//        Vector<StudyGroup> a  = (Vector<StudyGroup>) collectionManager.getCollection().clone();
//        for (StudyGroup i: a){
//            if (!i.validate()){
//                collectionManager.getCollection().remove(i);
//            }
//        }
//        Set<String> uniqueStrings = new HashSet<>();
//        String answer = "";
//
//
//        try {
//            while (true) {
//
//
//
//
//                if (!console.isCanReadln()){
//                    console.selectConsoleScanner();
//                    uniqueStrings.clear();
//                }
//                if (console.getFileScanner() == null) {
//                    System.out.print("Введите команду: ");
//                }
//                String[] userCommand = (console.readln().trim() + " 1 1 s ").split(" ", 4);
////                if (userCommand[0].equals("registration")) {
////                    System.out.println("Введите имя пользователя: ");
////                    String username = console.readln().trim();
////                    System.out.println("Введите пароль: ");
////                    String password = console.readln().trim();
////                    userCommand[2] = username;
////                    userCommand[3] = password;
////                }
////                if (userCommand[0].equals("login")) {
////                    System.out.println("Введите имя пользователя: ");
////                    String username = console.readln().trim();
////                    System.out.println("Введите пароль: ");
////                    String password = console.readln().trim();
////                    userCommand[2] = username;
////                    userCommand[3] = password;
////                }
//
//                if (userCommand[0].equals("exit")) {
//                    System.out.println("Выход из программы");
//                    break;
//                }
//                if (userCommand[0].equals("execute_skript")){
//                    while (console.isCanReadln()) {
//                        System.out.print("Введите название файла: ");
//                        inputString = console.readln().trim();
//                        File file = new File(inputString);
//                        if (file.isFile()) {
//                            break;
//                        }else{
//                            System.out.println("Нет такого файла");
//                        }
//
//                    }
//                    console.selectFileScanner(new Scanner(new FileReader(new File(inputString))));
//                    if (!uniqueStrings.contains(inputString)) {
//                        uniqueStrings.add(inputString);
//
//                    }else {
//                        console.selectConsoleScanner();
//                        uniqueStrings.clear();
//                        System.out.println("Произошло зацикливание скрипта");
////                        System.exit(1);
//                    }
//
//                }
//                if (!console.isCanReadln()){
//                    console.selectConsoleScanner();
//                }
//                if (userCommand[0].equals("registration")) {
//                    System.out.println("Введите имя пользователя: ");
//                    String username = console.readln().trim();
//                    System.out.println("Введите пароль: ");
//                    String password = console.readln().trim();
//                    userCommand[2] = username;
//                    userCommand[3] = password;
//                    processor.processCommand(userCommand);
//                } else if (userCommand[0].equals("login") && !processor.getAuthenticated()) {
//                    System.out.println("Введите имя пользователя: ");
//                    String username = console.readln().trim();
//                    System.out.println("Введите пароль: ");
//                    String password = console.readln().trim();
//                    userCommand[2] = username;
//                    userCommand[3] = password;
//                    processor.processCommand(userCommand);
//                } else if ((processor.getAuthenticated() && !userCommand[0].equals("login")) || (userCommand[0].equals("help")) ){
//                    processor.processCommand(userCommand);
//                } else if (!processor.getAuthenticated() && !userCommand[0].equals("login")) {
//                    System.out.println("Сначала авторизуйтесь!!!");
//                }
//
//
//            }
//        } catch (NoSuchElementException e) {
//            System.out.println("Напоминаю: у меня столько проблем вызвала эта дурацкая обработка CTRL + D, не нажимайте пожалуйста(((" );
//        } catch (FileNotFoundException e) {
//            System.out.println("Нет такого файла");
//        }
//
//    }
//}
import commands.CommandProcessor;
import models.StudyGroup;
import models.Response;
import utility.CollectionManager;
import utility.Console;
import utility.DumpManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner userScanner = new Scanner(System.in);

        var console = new StandartConsole();

        String inputString = "lab6file.txt";
        DumpManager dumpManager = new DumpManager(inputString, console);
        var collectionManager = new CollectionManager(dumpManager);
        CommandProcessor processor = new CommandProcessor(console, collectionManager);
        Vector<StudyGroup> a = (Vector<StudyGroup>) collectionManager.getCollection().clone();
        for (StudyGroup i : a) {
            if (!i.validate()) {
                collectionManager.getCollection().remove(i);
            }
        }
        Set<String> uniqueStrings = new HashSet<>();
        String answer = "";
        Integer userId = null;

        try {
            while (true) {
                if (!console.isCanReadln()) {
                    console.selectConsoleScanner();
                    uniqueStrings.clear();
                }
                if (console.getFileScanner() == null) {
                    System.out.print("Введите команду: ");
                }
                String[] userCommand = (console.readln().trim() + " 1 1 s ").split(" ", 4);

                if (userCommand[0].equals("exit")) {
                    System.out.println("Выход из программы");
                    break;
                }
                if (userCommand[0].equals("execute_skript")) {
                    while (console.isCanReadln()) {
                        System.out.print("Введите название файла: ");
                        inputString = console.readln().trim();
                        File file = new File(inputString);
                        if (file.isFile()) {
                            break;
                        } else {
                            System.out.println("Нет такого файла");
                        }
                    }
                    console.selectFileScanner(new Scanner(new FileReader(new File(inputString))));
                    if (!uniqueStrings.contains(inputString)) {
                        uniqueStrings.add(inputString);
                    } else {
                        console.selectConsoleScanner();
                        uniqueStrings.clear();
                        System.out.println("Произошло зацикливание скрипта");
                    }
                }
                if (!console.isCanReadln()) {
                    console.selectConsoleScanner();
                }
                if (userCommand[0].equals("registration")) {
                    System.out.println("Введите имя пользователя: ");
                    String username = console.readln().trim();
                    System.out.println("Введите пароль: ");
                    String password = console.readln().trim();
                    userCommand[2] = username;
                    userCommand[3] = password;
                    Response response = processor.processCommand(userCommand);
                    if (response != null && response.getExitCode()) {
                        userId = response.getUserId(); // Предполагается, что Response содержит userId
                    }
                    System.out.println(response.getMessage());
                } else if (userCommand[0].equals("login") && userId == null) {
                    System.out.println("Введите имя пользователя: ");
                    String username = console.readln().trim();
                    System.out.println("Введите пароль: ");
                    String password = console.readln().trim();
                    userCommand[2] = username;
                    userCommand[3] = password;
                    Response response = processor.processCommand(userCommand);
                    if (response != null && response.getExitCode()) {
                        userId = response.getUserId(); // Предполагается, что Response содержит userId
                    }
                    System.out.println(response.getMessage());
                } else if (userId != null) {
                    processor.setUserId(userId); // Устанавливаем userId для процессора команд
                    processor.processCommand(userCommand);
                } else {
                    System.out.println("Сначала авторизуйтесь!!!");
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Напоминаю: у меня столько проблем вызвала эта дурацкая обработка CTRL + D, не нажимайте пожалуйста(((");
        } catch (FileNotFoundException e) {
            System.out.println("Нет такого файла");
        }
    }
}