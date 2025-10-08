package models;

import models.Coordinates;
import models.FormOfEducation;
import models.Person;
import models.Semester;
import utility.Element;
import utility.Validatable;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class StudyGroup extends Element implements Validatable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long studentsCount; //Значение поля должно быть больше 0
    private FormOfEducation formOfEducation; //Поле может быть null
    private Semester semesterEnum; //Поле не может быть null
    private Person groupAdmin; //Поле может быть null

    public StudyGroup(int id, String name, LocalDateTime creationDate, Coordinates coordinates, Long studentsCount, FormOfEducation formOfEducation, Semester semester, Person person) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.coordinates = coordinates;
        this.studentsCount = studentsCount;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semester;
        this.groupAdmin = person;
    }
    public StudyGroup(Integer id, String name, Coordinates coordinates, Long studentsCount, FormOfEducation formOfEducation, Semester semesterEnum, Person groupAdmin) {
        this(id, name, LocalDateTime.now(), coordinates, studentsCount, formOfEducation, semesterEnum, groupAdmin);
    }
    @Override
    public String toString() {
        return "StudyGroup" + "\n" + "id: " + id +  "\n" +
                "Имя: " + name + "\n" +
                "Дата: " + creationDate.format(DateTimeFormatter.ISO_DATE_TIME) + "\n" +
                "Координаты: " + coordinates + "\n" +
                "Количество людей в группе: " + studentsCount + "\n" +
                "Форма обучения: " + (formOfEducation == null ? "null" : ""+formOfEducation+"") + "\n" +
                "Выбранный семестр: " + semesterEnum + "\n" + "Староста группы: " + (groupAdmin == null ? "null" : ""+groupAdmin+"") + "\n";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyGroup that = (StudyGroup) o;
        return Objects.equals(id, that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, studentsCount, formOfEducation, semesterEnum, groupAdmin);
    }
    @Override
    public Boolean validate() {
        if (id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (creationDate == null) return false;
        if (coordinates == null ) return false;
        if (studentsCount <= 0) return false;
        if (semesterEnum == null) return false;
        if (!groupAdmin.validate()){
            return false;
        }
        return true;
    }
    public int getId(){
        return id;
    }
    public Person getGroupAdmin(){
        return groupAdmin;
    }
    public Long getStudentsCount(){
        return studentsCount;
    }
    @Override
    public int compareTo(Element element){
        return this.id - element.getId();
    }
}