package utility;

import commands.Login;
import commands.StudyGroupDAO;
import models.StudyGroup;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

public class CollectionManager extends Thread implements Serializable {
    private static final long serialVersionUID = 1L;
    private int currentId = 1;
    private Map<Integer, StudyGroup> group = new HashMap<>();
    private Vector<StudyGroup> collection = new Vector<StudyGroup>();
//    StudyGroupDAO studyGroupDAO = new StudyGroupDAO();
//    private Vector<StudyGroup> collection = studyGroupDAO.getStudyGroupsByUserId(Login.getUserId());
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final DumpManager dumpManager;

    public CollectionManager(DumpManager dumpManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.dumpManager = dumpManager;
    }

    /**
     * @return Последнее время инициализации.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Последнее время сохранения.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return коллекция.
     */
    public Vector<StudyGroup> getCollection() {
        return collection;
    }
    public void setCollection(Vector<StudyGroup> newCollection) {
        this.collection = newCollection;
    }
    /**
     * Получить по ID
     */
    public StudyGroup byId(int id) {
        return group.get(id);

    }

    /**
     * Содержит ли колекции
     */
    public boolean isСontain(StudyGroup e) { return e == null || byId(e.getId()) != null; }

    /**
     * Получить свободный ID
     */
    public int getFreeId() {
        while (byId(++currentId) != null);
        return currentId;
    }

    /**
     * Добавляет
     */
    public boolean add(StudyGroup a) {
        if (isСontain(a)) return false;
        group.put(a.getId(), a);
        collection.add(a);
        update();
        return true;
    }


    /**
     * Обновляет
     */
    public boolean update(StudyGroup a) {
        if (!isСontain(a)) return false;
        collection.remove(byId(a.getId()));
        group.put(a.getId(), a);
        collection.add(a);
        update();
        return true;
    }


    /**
     * Удаляет  по ID
     */
    public boolean remove(int id) {
        var a = byId(id);
        if (a == null) return false;
        group.remove(a.getId());
        collection.remove(a);
        update();
        return true;
    }

    /**
     * Фиксирует изменения коллекции
     */
    public void update() {
        Collections.sort(collection);
    }

    public boolean loadCollection() {
        group.clear();
        collection = dumpManager.readCollection();
        lastInitTime = LocalDateTime.now();
        for (var e : collection)
            if (byId(e.getId()) != null) {
                collection.clear();
                return false;
            } else {
                if (e.getId()>currentId) currentId = e.getId();
                group.put(e.getId(), e);
            }
        update();
        return true;
    }

    /**
     * Сохраняет коллекцию в файл
     */
    public void saveCollection() {
        dumpManager.writeCollection(collection);
        lastSaveTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";

        StringBuilder info = new StringBuilder();
        for (var aboba : collection) {
            info.append(aboba+"\n\n");
        }
        return info.toString().trim();
    }
}
