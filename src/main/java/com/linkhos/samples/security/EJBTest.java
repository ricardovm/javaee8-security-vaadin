package com.linkhos.samples.security;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;

@Stateless
public class EJBTest {

    @Resource
    private EJBContext ctx;

    @Inject
    private SessionInfo sessaoUsuario;

    public String getHello() {
        return "Hello " + ctx.getCallerPrincipal().getName()
                + ":" + Optional.ofNullable(sessaoUsuario).map(SessionInfo::getLogin).orElse("(null)");
    }
}
