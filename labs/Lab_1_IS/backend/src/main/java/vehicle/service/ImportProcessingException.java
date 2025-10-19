package vehicle.service;

import java.util.List;

public class ImportProcessingException extends RuntimeException {
    private final List<String> errors;

    public ImportProcessingException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}

