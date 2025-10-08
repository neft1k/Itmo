package utility;

import java.io.Serializable;

public abstract class Element implements Comparable<Element>, Validatable, Serializable {
    private static final long serialVersionUID = 1L;
    abstract public int getId();
}
