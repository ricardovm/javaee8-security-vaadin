package com.linkhos.samples.security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

import static javax.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;

@ApplicationScoped
public class MyHttpAuthenticationMechanism implements HttpAuthenticationMechanism {

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response,
                                                HttpMessageContext httpMessageContext) {

        var session = request.getSession(false);

        if (session != null
                && session.getAttribute("AUTH_USER") != null
                && session.getAttribute("AUTH_GROUPS") != null) {
            var username = (String) session.getAttribute("AUTH_USER");
            var groups = (Set<String>) session.getAttribute("AUTH_GROUPS");

            return httpMessageContext.notifyContainerAboutLogin(
                    new CredentialValidationResult(username, groups));
        } else if (request.getParameter("username") != null) {
            var result = identityStoreHandler.validate(
                    new UsernamePasswordCredential(
                            request.getParameter("username"),
                            request.getParameter("password")));

            return result.getStatus() == VALID
                    ? httpMessageContext.notifyContainerAboutLogin(result)
                    : httpMessageContext.responseUnauthorized();
        } else {
            return httpMessageContext.notifyContainerAboutLogin((String) null, Set.of());
        }
    }
}
