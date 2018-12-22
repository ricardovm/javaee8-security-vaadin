package com.linkhos.samples.security;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Title;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.*;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.IdentityStoreHandler;

import java.security.Principal;

import static javax.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;

@Title("Security")
@CDIUI("")
@Push(transport = Transport.WEBSOCKET_XHR)
public class MainUI extends UI {

    private Label errorLabel;
    private TextField usernameField;
    private PasswordField passwordField;
    private CheckBox rememberMeCheckBox;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private Principal principal;

    @Inject
    IdentityStoreHandler identityStoreHandler;

    @Inject
    SessionInfo sessionInfo;

    @Inject
    Instance<VaadinTestDialog> vaadinTestDialog;

    @Override
    protected void init(VaadinRequest request) {
        if (securityContext.getCallerPrincipal() != null) {
            setContent(buildMainUI());
        } else {
            setContent(buildLoginUI());
        }
    }

    private Component buildMainUI() {
        var helloLabel = new Label(String.format("Hello %s!", securityContext.getCallerPrincipal().getName()));

        var sessionLabel = new Label(String.format("Hello session %s!", sessionInfo.getLogin()));

        var principalLabel = new Label(String.format("Hello principal %s!", principal.getName()));

        var testButton = new Button("Test");
        testButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        testButton.addClickListener(e -> UI.getCurrent().addWindow(vaadinTestDialog.get()));

        var logoutButton = new Button("Logout");
        logoutButton.addStyleName(ValoTheme.BUTTON_DANGER);
        logoutButton.addClickListener(e -> {
            VaadinSession.getCurrent().getSession().invalidate();
            Page.getCurrent().reload();
        });

        return new VerticalLayout(
                helloLabel,
                sessionLabel,
                principalLabel,
                new HorizontalLayout(testButton, logoutButton));
    }

    private Component buildLoginUI() {
        var mainLayout = new VerticalLayout();

        errorLabel = new Label("Username and/or password invalid");
        errorLabel.addStyleName(ValoTheme.LABEL_FAILURE);
        errorLabel.setVisible(false);
        mainLayout.addComponent(errorLabel);

        var formLayout = new FormLayout();
        mainLayout.addComponent(formLayout);

        usernameField = new TextField("Username");
        usernameField.focus();
        formLayout.addComponent(usernameField);

        passwordField = new PasswordField("Password");
        formLayout.addComponent(passwordField);

        rememberMeCheckBox = new CheckBox("Remember me");
        formLayout.addComponent(rememberMeCheckBox);

        var enterButton = new Button("Enter", e -> doLogin());
        enterButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        mainLayout.addComponent(enterButton);

        return mainLayout;
    }

    private void doLogin() {
        var loginValidation = identityStoreHandler.validate(
                new UsernamePasswordCredential(usernameField.getValue(), passwordField.getValue()));

        if (loginValidation.getStatus() == VALID) {
            WrappedSession session = VaadinSession.getCurrent().getSession();
            session.setAttribute("AUTH_USER", loginValidation.getCallerPrincipal().getName());
            session.setAttribute("AUTH_GROUPS", loginValidation.getCallerGroups());

            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());

            Page.getCurrent().reload();
        } else {
            errorLabel.setVisible(true);

            var notification = new Notification("Security", errorLabel.getValue(),
                    Notification.Type.ERROR_MESSAGE);
            notification.setPosition(Position.TOP_CENTER);
            notification.setDelayMsec(2500);
            notification.show(Page.getCurrent());
        }
    }
}
