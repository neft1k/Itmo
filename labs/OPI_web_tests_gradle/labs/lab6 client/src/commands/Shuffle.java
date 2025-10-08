package commands;

import utility.CollectionManager;
import utility.Console;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;

public class Shuffle extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Shuffle(String name, String description) {
        super("shuffle", "перемешать элементы коллекции в случайном порядке");
    }

}
