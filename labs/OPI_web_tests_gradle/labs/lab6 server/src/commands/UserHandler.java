package commands;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Random;

public class UserHandler {
    private static final String PEPPER = "randomPepperValue";
    private static final Random RANDOM = new Random();

    public static String registerUser(String username, String password) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt, PEPPER);

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO users (username, password, salt) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, hashedPassword);
                statement.setString(3, salt);

                statement.executeUpdate();
                return "Регистрация прошла успешно!";
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // SQLState for unique constraint violation
                return "Ошибка: Пользователь с таким именем уже существует.";
            }
            e.printStackTrace();
            return "Ошибка при регистрации.";
        }
    }

    public static AuthenticationResult authenticateUser(String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT id, password, salt FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int userId = resultSet.getInt("id");
                        String storedHashedPassword = resultSet.getString("password");
                        String salt = resultSet.getString("salt");
                        String hashedPassword = hashPassword(password, salt, PEPPER);
                        if (storedHashedPassword.equals(hashedPassword)) {
                            return new AuthenticationResult(userId,"Вы успешно вошли!");
                        } else {
                            return new AuthenticationResult(-1,"Неверное имя пользователя или пароль.");
                        }
                    } else {
                        return new AuthenticationResult(-1,"Неверное имя пользователя или пароль.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new AuthenticationResult(-1,"Ошибка при аутентификации.");
        }
    }


    private static String generateSalt() {
        byte[] saltBytes = new byte[16];
        RANDOM.nextBytes(saltBytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : saltBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static String hashPassword(String password, String salt, String pepper) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-224");
            md.update((salt + password + pepper).getBytes());
            byte[] hashedBytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static class AuthenticationResult {
        private final int userId;
        private final String message;

        public AuthenticationResult(int userId, String message) {
            this.userId = userId;
            this.message = message;
        }

        public int getUserId() {
            return userId;
        }

        public String getMessage() {
            return message;
        }
    }

}
