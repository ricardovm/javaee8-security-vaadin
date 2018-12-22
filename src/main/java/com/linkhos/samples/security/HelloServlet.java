package com.linkhos.samples.security;

import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@WebServlet(urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    @Inject
    private SecurityContext securityContext;

    @Inject
    private Principal principal;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");

        var pw = resp.getWriter();

        if (securityContext.getCallerPrincipal() != null) {
            pw.println("Hello " + securityContext.getCallerPrincipal().getName());
            pw.println("Hello again " + principal.getName());
        } else {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
