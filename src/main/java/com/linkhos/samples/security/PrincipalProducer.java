package com.linkhos.samples.security;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import java.security.Principal;

/*
 * Not working on WildFly 15 without this producer... will check again later.
 */
@Alternative
@RequestScoped
public class PrincipalProducer {

    @Inject
    private SecurityContext securityContext;

    @Produces
    public Principal getPrincipal() {
        return securityContext.getCallerPrincipal();
    }
}
