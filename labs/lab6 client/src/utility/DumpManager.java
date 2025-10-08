package utility;

import com.google.gson.*;
import models.StudyGroup;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Vector;

import com.google.gson.reflect.TypeToken;
import models.StudyGroup;

public class DumpManager {
    private final String fileName;
    private final Console console;

    public DumpManager(String fileName, Console console) {
        this.fileName = fileName;
        this.console = console;
    }
        public void writeCollection(Collection<StudyGroup> collection) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
            Gson gson = gsonBuilder.setPrettyPrinting().create();
            JsonArray jsonArray = new JsonArray();

            for (Object obj : collection) {
                JsonElement jsonElement = gson.toJsonTree(obj);
                jsonArray.add(jsonElement);
            }

            try (FileWriter fileWriter = new FileWriter(fileName)) {
                gson.toJson(jsonArray, fileWriter);
                System.out.println("Коллекция добавлена в файл: " + fileName);
            } catch (IOException e) {
                System.err.println("Ошибка записи коллекции в файл: " + e.getMessage());
            }
        }


//    public void writeCollection(Collection<StudyGroup> collection) {
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
//        Gson gson = gsonBuilder.setPrettyPrinting().create();
//        try (PrintWriter collectionPrintWriter = new PrintWriter(fileName)) {
//
//            collectionPrintWriter.println(gson.toJson(collection));
//            console.println("Коллекция успешна сохранена в файл!");
//        } catch (IOException exception) {
//            console.printError("Загрузочный файл не может быть открыт!");
//        }
//    }

//    public void writeCollectionToFile(Collection<StudyGroup> collection) {
//        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
//             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
//
//            objectOutputStream.writeObject(gson.toJson(collection));
//            System.out.println("Коллекция успешно записана в файл: " + fileName);
//
//        } catch (IOException e) {
//            System.err.println("Ошибка при записи коллекции в файл: " + e.getMessage());
//        }
//    }

    public Vector<StudyGroup> readCollection() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        if (fileName != null && !fileName.isEmpty()) {
            try (var fileReader = new FileReader(fileName)) {
                var collectionType = new TypeToken<Vector<StudyGroup>>() {}.getType();
                var reader = new BufferedReader(fileReader);

                var jsonString = new StringBuilder();

                String line;
                while((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.equals("")) {
                        jsonString.append(line);
                    }
                }

                if (jsonString.length() == 0) {
                    jsonString = new StringBuilder("[]");
                }

                Vector<StudyGroup> collection = gson.fromJson(jsonString.toString(), collectionType);

                console.println("Коллекция успешна загружена!");
                return collection;

            } catch (FileNotFoundException exception) {
                console.printError("Загрузочный файл не найден!");
            } catch (NoSuchElementException exception) {
                console.printError("Загрузочный файл пуст!");
            } catch (JsonParseException exception) {
                console.printError("В загрузочном файле не обнаружена необходимая коллекция!");
            } catch (IllegalStateException | IOException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        } else {
            console.printError("Аргумент командной строки с загрузочным файлом не найден!");
        }
        return new Vector<>();
    }
}
