package commands;

import java.io.Serial;
import java.io.Serializable;

public class Login extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private String user;
    public Login(String name, String description, String user) {
        super("login", "авторизация");
        this.name = name;
        this.description = description;
        this.user = user;
    }
}
