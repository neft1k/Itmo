package vehicle.security;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

@RequestScoped
public class CurrentUser {

    @Inject
    HttpServletRequest request;

    public CurrentUser() {}

    public String username() {
        String username = headerOrParam("X-User", "user");
        if (username == null || username.isBlank()) {
            return "anonymous";
        }
        return username.trim();
    }

    public String role() {
        String role = headerOrParam("X-Role", "role");
        if (role == null || role.isBlank()) {
            return "user";
        }
        return role.trim().toLowerCase();
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role());
    }

    private String headerOrParam(String header, String param) {
        String value = request.getHeader(header);
        if (value != null) {
            return value;
        }
        return request.getParameter(param);
    }
}
