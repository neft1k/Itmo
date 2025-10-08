package commands;

import utility.CollectionManager;
import utility.Console;

public class Save extends Command {

    public Save(String name, String description) {
        super("save", "сохраняет коллекцию в файл");
    }
    public static void execution(Console console, CollectionManager collectionManager){
        collectionManager.saveCollection();
        System.out.println("Коллекция сохранилась в файле");
    }
}
