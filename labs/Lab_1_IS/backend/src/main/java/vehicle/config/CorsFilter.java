package vehicle.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter, ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            Response.ResponseBuilder rb = Response.ok();
            rb.header("Access-Control-Allow-Origin", getOrigin(requestContext));
            rb.header("Vary", "Origin");
            rb.header("Access-Control-Allow-Credentials", "false");
            rb.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            rb.header("Access-Control-Allow-Headers",
                    "Origin, Content-Type, Accept, X-Requested-With, Cache-Control, Pragma, Authorization");
            rb.header("Access-Control-Max-Age", "86400");
            requestContext.abortWith(rb.build());
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", getOrigin(requestContext));
        responseContext.getHeaders().putSingle("Vary", "Origin");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "false");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Headers",
                "Origin, Content-Type, Accept, X-Requested-With, Cache-Control, Pragma, Authorization");
    }

    private String getOrigin(ContainerRequestContext ctx) {
        String origin = ctx.getHeaderString("Origin");
        return origin != null ? origin : "*";
    }
}
