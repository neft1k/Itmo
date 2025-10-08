package commands;


import models.*;

import java.sql.*;
import java.util.Vector;

//public class StudyGroupDAO {
//    public void saveStudyGroup(StudyGroup studyGroup, int userId) {
//        String query = "INSERT INTO study_groups (name, creation_date, coordinates, students_count, form_of_education, semester_enum, group_admin_name, group_admin_passport_id, group_admin_eye_color, group_admin_hair_color, group_admin_nationality, location, users_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//
//            statement.setString(1, studyGroup.getName());
//            statement.setTimestamp(2, java.sql.Timestamp.valueOf(studyGroup.getCreationDate()));
//            String coordinates = studyGroup.getCoordinates().getX() + "," + studyGroup.getCoordinates().getY();
//            statement.setString(3, coordinates);
//            statement.setLong(4, studyGroup.getStudentsCount());
////            statement.setString(5, studyGroup.getFormOfEducation().toString());
//            statement.setString(5, studyGroup.getFormOfEducation() != null ? studyGroup.getFormOfEducation().toString() : null);
//            statement.setString(6, studyGroup.getSemesterEnum().toString());
//            statement.setString(7, studyGroup.getGroupAdmin().getName());
//            statement.setString(8, studyGroup.getGroupAdmin().getPassportID());
//            statement.setString(9, studyGroup.getGroupAdmin().getEyeColor().toString());
//            statement.setString(10, studyGroup.getGroupAdmin().getHairColor() != null ? studyGroup.getGroupAdmin().getHairColor().toString() : null);
//            statement.setString(11, studyGroup.getGroupAdmin().getNationality() != null ? studyGroup.getGroupAdmin().getNationality().toString() : null);
//            String location = studyGroup.getGroupAdmin().getLocation().getName() + "," + studyGroup.getGroupAdmin().getLocation().getX() + "," + studyGroup.getGroupAdmin().getLocation().getY();
//            statement.setString(12, location);
//            statement.setInt(13, userId);
//
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            if (e.getSQLState().equals("23505")) { // SQLState for unique constraint violation
//                System.out.println("Ошибка: Пользователь с таким именем уже существует.");
//            } else {
//                e.printStackTrace();
//            }
//        }
//    }
//    public Vector<StudyGroup> getStudyGroupsByUserId(int userId) {
//        String query = "SELECT * FROM study_groups WHERE users_id = ?";
//        Vector<StudyGroup> studyGroups = new Vector<>();
//
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//
//            statement.setInt(1, userId);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                while (resultSet.next()) {
//                    int id = resultSet.getInt("id");
//                    String name = resultSet.getString("name");
//                    Timestamp creationDate = resultSet.getTimestamp("creation_date");
//                    String[] coordinate = resultSet.getString("coordinates").split(",");
//                    int coordinatesX = Integer.parseInt(coordinate[0]);
//                    long coordinatesY = Long.parseLong(coordinate[1]);
//                    Long studentsCount = resultSet.getLong("students_count");
////                    String formOfEducation = resultSet.getString("form_of_education");
//                    String formOfEducationStr = resultSet.getString("form_of_education");
//                    FormOfEducation formOfEducation = formOfEducationStr != null ? FormOfEducation.valueOf(formOfEducationStr) : null;
//                    String semesterEnum = resultSet.getString("semester_enum");
////                    int groupAdminId = resultSet.getInt("group_admin_id");
//                    String groupAdminName = resultSet.getString("group_admin_name");
//                    String groupAdminPassportId = resultSet.getString("group_admin_passport_id");
//                    String groupAdminEyeColor = resultSet.getString("group_admin_eye_color");
////                    String groupAdminHairColor = resultSet.getString("group_admin_hair_color");
//                    String groupAdminHairColorStr = resultSet.getString("group_admin_hair_color");
//                    HairColor groupAdminHairColor = groupAdminHairColorStr != null ? HairColor.valueOf(groupAdminHairColorStr) : null;
////                    String groupAdminNationality = resultSet.getString("group_admin_nationality");
//                    String groupAdminNationalityStr = resultSet.getString("group_admin_nationality");
//                    Country groupAdminNationality = groupAdminNationalityStr != null ? Country.valueOf(groupAdminNationalityStr) : null;
//                    String[] location = resultSet.getString("location").split(",");
//                    String locationName = location[0];
//                    int locationX = Integer.parseInt(location[1]);
//                    int locationY = Integer.parseInt(location[2]);
//
//
//                    Coordinates coordinates = new Coordinates(coordinatesX, coordinatesY);
//                    Person groupAdmin = new Person(groupAdminName, groupAdminPassportId, EyeColor.valueOf(groupAdminEyeColor), groupAdminHairColor, groupAdminNationality, new Location(locationName, locationX, locationY));
//
//
//                    StudyGroup studyGroup = new StudyGroup(id, name, creationDate.toLocalDateTime(), coordinates, studentsCount, formOfEducation, Semester.valueOf(semesterEnum), groupAdmin);
//                    studyGroups.add(studyGroup);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Проблема при получении объекта из бд");
//        }
//
//        return studyGroups;
//    }
//    public boolean deleteStudyGroupById(int id, int userId) {
//        String query = "DELETE FROM study_groups WHERE id = ? AND users_id = ?";
//
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//
//            statement.setInt(1, id);
//            statement.setInt(2, userId);
//            int rowsAffected = statement.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//    public boolean clearStudyGroupById(int userId) {
//        String query = "DELETE FROM study_groups WHERE users_id = ?";
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, userId);
//            int rowsAffected = statement.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean deleteStudyGroupWithMinId(int userId) {
//        String selectQuery = "SELECT id FROM study_groups WHERE users_id = ? ORDER BY id ASC LIMIT 1";
//        String deleteQuery = "DELETE FROM study_groups WHERE id = ?";
//
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
//             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
//            selectStatement.setInt(1, userId);
//            try (ResultSet resultSet = selectStatement.executeQuery()) {
//                if (resultSet.next()) {
//                    int minId = resultSet.getInt("id");
//
//                    deleteStatement.setInt(1, minId);
//                    int rowsAffected = deleteStatement.executeUpdate();
//                    return rowsAffected > 0;
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//        return false;
//    }
//}
import models.StudyGroup;
//import utility.DatabaseConnection;

import java.sql.*;
import java.util.Vector;

public class StudyGroupDAO {
    public void saveStudyGroup(StudyGroup studyGroup, int userId) {
        String query = "INSERT INTO study_groups (name, creation_date, coordinates, students_count, form_of_education, semester_enum, group_admin_name, group_admin_passport_id, group_admin_eye_color, group_admin_hair_color, group_admin_nationality, location, users_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, studyGroup.getName());
            statement.setTimestamp(2, java.sql.Timestamp.valueOf(studyGroup.getCreationDate()));
            String coordinates = studyGroup.getCoordinates().getX() + "," + studyGroup.getCoordinates().getY();
            statement.setString(3, coordinates);
            statement.setLong(4, studyGroup.getStudentsCount());
            statement.setString(5, studyGroup.getFormOfEducation() != null ? studyGroup.getFormOfEducation().toString() : null);
            statement.setString(6, studyGroup.getSemesterEnum().toString());
            statement.setString(7, studyGroup.getGroupAdmin().getName());
            statement.setString(8, studyGroup.getGroupAdmin().getPassportID());
            statement.setString(9, studyGroup.getGroupAdmin().getEyeColor().toString());
            statement.setString(10, studyGroup.getGroupAdmin().getHairColor() != null ? studyGroup.getGroupAdmin().getHairColor().toString() : null);
            statement.setString(11, studyGroup.getGroupAdmin().getNationality() != null ? studyGroup.getGroupAdmin().getNationality().toString() : null);
            String location = studyGroup.getGroupAdmin().getLocation().getName() + "," + studyGroup.getGroupAdmin().getLocation().getX() + "," + studyGroup.getGroupAdmin().getLocation().getY();
            statement.setString(12, location);
            statement.setInt(13, userId);

            statement.executeUpdate();
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                System.out.println("Ошибка: Пользователь с таким именем уже существует.");
            } else {
                e.printStackTrace();
            }
        }
    }

    public Vector<StudyGroup> getStudyGroupsByUserId() {
        String query = "SELECT * FROM study_groups";
        Vector<StudyGroup> studyGroups = new Vector<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

//            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    Timestamp creationDate = resultSet.getTimestamp("creation_date");
                    String[] coordinate = resultSet.getString("coordinates").split(",");
                    int coordinatesX = Integer.parseInt(coordinate[0]);
                    long coordinatesY = Long.parseLong(coordinate[1]);
                    Long studentsCount = resultSet.getLong("students_count");
                    String formOfEducationStr = resultSet.getString("form_of_education");
                    FormOfEducation formOfEducation = formOfEducationStr != null ? FormOfEducation.valueOf(formOfEducationStr) : null;
                    String semesterEnum = resultSet.getString("semester_enum");
                    String groupAdminName = resultSet.getString("group_admin_name");
                    String groupAdminPassportId = resultSet.getString("group_admin_passport_id");
                    String groupAdminEyeColor = resultSet.getString("group_admin_eye_color");
                    String groupAdminHairColorStr = resultSet.getString("group_admin_hair_color");
                    HairColor groupAdminHairColor = groupAdminHairColorStr != null ? HairColor.valueOf(groupAdminHairColorStr) : null;
                    String groupAdminNationalityStr = resultSet.getString("group_admin_nationality");
                    Country groupAdminNationality = groupAdminNationalityStr != null ? Country.valueOf(groupAdminNationalityStr) : null;
                    String[] location = resultSet.getString("location").split(",");
                    int userId = resultSet.getInt("users_id");
                    String locationName = location[0];
                    int locationX = Integer.parseInt(location[1]);
                    int locationY = Integer.parseInt(location[2]);

                    Coordinates coordinates = new Coordinates(coordinatesX, coordinatesY);
                    Person groupAdmin = new Person(groupAdminName, groupAdminPassportId, EyeColor.valueOf(groupAdminEyeColor), groupAdminHairColor, groupAdminNationality, new Location(locationName, locationX, locationY));

                    StudyGroup studyGroup = new StudyGroup(id, name, creationDate.toLocalDateTime(), coordinates, studentsCount, formOfEducation, Semester.valueOf(semesterEnum), groupAdmin, userId);
                    studyGroups.add(studyGroup);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Проблема при получении объекта из бд");
        }

        return studyGroups;
    }
    public StudyGroup getObjectByUserId() {
        String query = "SELECT * FROM study_groups ORDER BY id DESC LIMIT 1";
        StudyGroup studyGroup = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

//            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    Timestamp creationDate = resultSet.getTimestamp("creation_date");
                    String[] coordinate = resultSet.getString("coordinates").split(",");
                    int coordinatesX = Integer.parseInt(coordinate[0]);
                    long coordinatesY = Long.parseLong(coordinate[1]);
                    Long studentsCount = resultSet.getLong("students_count");
                    String formOfEducationStr = resultSet.getString("form_of_education");
                    FormOfEducation formOfEducation = formOfEducationStr != null ? FormOfEducation.valueOf(formOfEducationStr) : null;
                    String semesterEnum = resultSet.getString("semester_enum");
                    String groupAdminName = resultSet.getString("group_admin_name");
                    String groupAdminPassportId = resultSet.getString("group_admin_passport_id");
                    String groupAdminEyeColor = resultSet.getString("group_admin_eye_color");
                    String groupAdminHairColorStr = resultSet.getString("group_admin_hair_color");
                    HairColor groupAdminHairColor = groupAdminHairColorStr != null ? HairColor.valueOf(groupAdminHairColorStr) : null;
                    String groupAdminNationalityStr = resultSet.getString("group_admin_nationality");
                    Country groupAdminNationality = groupAdminNationalityStr != null ? Country.valueOf(groupAdminNationalityStr) : null;
                    String[] location = resultSet.getString("location").split(",");
                    int userId = resultSet.getInt("users_id");
                    String locationName = location[0];
                    int locationX = Integer.parseInt(location[1]);
                    int locationY = Integer.parseInt(location[2]);

                    Coordinates coordinates = new Coordinates(coordinatesX, coordinatesY);
                    Person groupAdmin = new Person(groupAdminName, groupAdminPassportId, EyeColor.valueOf(groupAdminEyeColor), groupAdminHairColor, groupAdminNationality, new Location(locationName, locationX, locationY));

                    studyGroup = new StudyGroup(id, name, creationDate.toLocalDateTime(), coordinates, studentsCount, formOfEducation, Semester.valueOf(semesterEnum), groupAdmin, userId);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Проблема при получении объекта из бд");
        }

        return studyGroup;
    }

    public boolean deleteStudyGroupById(int id, int userId) {
        String query = "DELETE FROM study_groups WHERE id = ? AND users_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.setInt(2, userId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearStudyGroupById(int userId) {
        String query = "DELETE FROM study_groups WHERE users_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudyGroupWithMinId(int userId) {
        String selectQuery = "SELECT id FROM study_groups WHERE users_id = ? ORDER BY id ASC LIMIT 1";
        String deleteQuery = "DELETE FROM study_groups WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            selectStatement.setInt(1, userId);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    int minId = resultSet.getInt("id");

                    deleteStatement.setInt(1, minId);
                    int rowsAffected = deleteStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }
}