package commands;

import java.io.Serial;
import java.io.Serializable;

public class Registration extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private String user;
    public Registration(String name, String description, String user) {
        super("registration", "регистрация");
        this.name = name;
        this.description = description;
        this.user = user;
    }
}
