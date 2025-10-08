import commands.*;

import models.StudyGroup;
import utility.CollectionManager;
import utility.DatabaseConfig;
import utility.DumpManager;
import utility.StandartConsole;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

import static commands.Server.*;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        var console = new StandartConsole();




        DumpManager dumpManager = new DumpManager("lab6file.json", console);
        CollectionManager collectionManager = new CollectionManager(dumpManager);
        Server.setCollectionManager(collectionManager);
        collectionManager.loadCollection();

        StudyGroupDAO studyGroupDAO = new StudyGroupDAO();

        collectionManager.setCollection(studyGroupDAO.getStudyGroupsByUserId());


        Server.setProcessor( new CommandProcessor(console, collectionManager));
        Server server = new Server();
        server.start();
        while (true) {
            if (console.getFileScanner() == null) {
                System.out.print("Введите команду: ");
            }
            String[] userCommand = (console.readln().trim() + " 1 1 s ").split(" ", 4);
            if (Objects.equals(userCommand[0], "save")){
                server.interrupt();
            }
            if (Objects.equals(userCommand[0], "exit")){
                server.interrupt();
                server.stop();
                break;
            }
        }
    }
}