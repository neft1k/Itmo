package vehicle.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.*;

@Provider
public class ConstraintViolationMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException ex) {
        List<Map<String,String>> errors = new ArrayList<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            Map<String,String> e = new HashMap<>();
            e.put("field", v.getPropertyPath().toString());
            e.put("message", v.getMessage());
            errors.add(e);
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("errors", errors)).build();
    }
}
