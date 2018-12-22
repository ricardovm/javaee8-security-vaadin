package com.linkhos.samples.security;

import com.vaadin.cdi.VaadinSessionScoped;

import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.transaction.Transactional;
import java.security.Principal;

@Transactional
@VaadinSessionScoped
public class SessionInfo {

    private static final String NOT_LOGGED_IN = "(not logged in)";

    private String login;

    @Inject
    private SecurityContext securityContext;

    public String getLogin() {
        var principal = securityContext.getCallerPrincipal();

        if (isLoginValid(principal)) {
            login = principal.getName();
            return principal.getName();
        } else {
            login = null;
            return NOT_LOGGED_IN;
        }
    }

    private static boolean isLoginValid(Principal principal) {
        return principal != null
                && principal.getName() != null
                && !"anonymous".equalsIgnoreCase(principal.getName());
    }
}
