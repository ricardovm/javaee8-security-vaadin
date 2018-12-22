package com.linkhos.samples.security;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import java.security.Principal;

public class VaadinTestDialog extends Window {

    @Inject
    private SessionInfo sessionInfo;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private Principal principal;

    @Inject
    private EJBTest ejbTest;

    @Inject
    private CDITest cdiTest;

    @PostConstruct
    protected void init() {
        this.setModal(true);
        this.center();
        this.setResizable(false);

        setCaption("User info");

        var mainLayout = new VerticalLayout();
        mainLayout.setWidthUndefined();
        setContent(mainLayout);

        var formLayout = new FormLayout();
        mainLayout.addComponent(formLayout);

        formLayout.addComponent(newLabel("session", VaadinRequest.getCurrent().getWrappedSession().getId()));
        formLayout.addComponent(newLabel("vaadinSession Token", VaadinSession.getCurrent().getCsrfToken()));
        formLayout.addComponent(newLabel("vaadinSession PushId", VaadinSession.getCurrent().getPushId()));
        formLayout.addComponent(newLabel("sessionInfo", sessionInfo.getLogin()));
        formLayout.addComponent(newLabel("securityContext", securityContext.getCallerPrincipal().getName()));
        formLayout.addComponent(newLabel("principal", principal.getName()));
        formLayout.addComponent(newLabel("EJB", ejbTest.getHello()));
        formLayout.addComponent(newLabel("CDI", cdiTest.getHello()));
//        formLayout.addComponent(newLabel("IP", sessionInfo.getIp()));
    }

    private Label newLabel(String caption, String value) {
        Label label = new Label(value);
        label.setCaption(caption);
        return label;
    }
}
