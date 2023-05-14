package ip.labwork.util.validation;

import java.util.Set;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Set<String> errors) {
        super(String.join("\n", errors));
    }
}
