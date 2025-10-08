package commands;


import models.StudyGroup;
import utility.Answer;
import utility.CollectionManager;
import utility.Console;

import java.io.Serializable;

public abstract class Command<T> implements Describable, Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final String description;
    private  StudyGroup studyGroup ;
    private  Integer id;
    private String user;
    private Integer client;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @return Название и использование команды.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Описание команды.
     */
    public String getDescription() {
        return description;
    }
    public StudyGroup getStudyGroup(){
        return studyGroup;
    }
    public Integer getId(){return id;}

    public Integer getClient() {
        return client;
    }

    public String getUser(){return user;}
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (obj == null || getClass() != obj.getClass()) return false;
//        Command command = (Command) obj;
//        return name.equals(command.name) && description.equals(command.description);
//    }
//
//    @Override
//    public int hashCode() {
//        return name.hashCode() + description.hashCode();
//    }
//



    public abstract Useless execution(T t, Integer userId) throws Answer.AnswerBreak;


    public abstract Useless execution(String[] command, String user);

    public abstract Useless execution(String[] command, Integer userId);
    public abstract Useless execution(String[] strings, StudyGroup studyGroup, Integer userId);
    public abstract Useless execution(String[] strings, StudyGroup studyGroup, Integer id, Integer userId);
    public abstract Useless execution(String[] strings, Integer id, Integer userId);
    public abstract String execution();
    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
