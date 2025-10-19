package vehicle.web;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import vehicle.exception.UniqueConstraintViolationException;

import java.util.Map;

@Provider
public class UniqueConstraintExceptionMapper implements ExceptionMapper<UniqueConstraintViolationException> {
    @Override
    public Response toResponse(UniqueConstraintViolationException exception) {
        return Response.status(Response.Status.CONFLICT)
                .entity(Map.of("message", exception.getMessage()))
                .build();
    }
}

