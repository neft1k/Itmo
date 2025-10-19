package vehicle.web;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String ALLOWED_HEADERS = Arrays.asList(
            "origin",
            "content-type",
            "accept",
            "authorization",
            "x-user",
            "x-role"
    ).stream().collect(Collectors.joining(", "));

    private static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, OPTIONS";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            requestContext.abortWith(Response.ok().build());
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String origin = requestContext.getHeaderString("Origin");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", origin == null ? "*" : origin);
        responseContext.getHeaders().putSingle("Vary", "Origin");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "false");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", ALLOWED_HEADERS);
        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", ALLOWED_METHODS);
        responseContext.getHeaders().putSingle("Access-Control-Max-Age", "3600");
    }
}

