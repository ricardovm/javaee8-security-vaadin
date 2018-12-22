package com.linkhos.samples.security;

import javax.inject.Inject;
import java.security.Principal;

public class CDITest {

    @Inject
    private Principal principal;

    public String getHello() {
        return "Hello " + principal.getName();
    }
}
