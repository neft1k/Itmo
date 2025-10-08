package models;

import java.io.Serial;
import java.io.Serializable;

//public class Response implements Serializable {
//
//    private boolean exitCode;
//    private String message;
//    @Serial
//    private static final long serialVersionUID = 1L;
//
//    public Response(boolean code, String message) {
//        exitCode = code;
//        this.message = message;
//    }
//    public Response(String message) {
//        this.message = message;
//    }
//
//    public boolean getExitCode() { return exitCode; }
//    public String getMessage() { return message; }
//    public String toString() { return String.valueOf(exitCode)+";"+message; }
//}
import java.io.Serial;
import java.io.Serializable;

import java.io.Serial;
import java.io.Serializable;

public class Response implements Serializable {

    private boolean exitCode;
    private String message;
    private Integer userId; // Новое поле для userId
    @Serial
    private static final long serialVersionUID = 1L;

    // Конструктор с exitCode и message
    public Response(boolean code, String message) {
        this.exitCode = code;
        this.message = message;
    }

    // Конструктор с message
    public Response(String message) {
        this.message = message;
    }

    // Новый конструктор с message и userId
    public Response(String message, Integer userId) {
        this.message = message;
        this.userId = userId;
    }

    // Новый конструктор с exitCode, message и userId
    public Response(boolean code, String message, Integer userId) {
        this.exitCode = code;
        this.message = message;
        this.userId = userId;
    }

    // Геттер для exitCode
    public boolean getExitCode() {
        return exitCode;
    }

    // Геттер для message
    public String getMessage() {
        return message;
    }

    // Геттер для userId
    public Integer getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return String.valueOf(exitCode) + ";" + message;
    }
}