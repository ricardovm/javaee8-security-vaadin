package com.linkhos.samples.security;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Arrays;
import java.util.HashSet;

import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;

@ApplicationScoped
public class MyIdentityStore implements IdentityStore {

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
        if (credential.compareTo("admin", "1234")) {
            return new CredentialValidationResult("admin",
                    new HashSet<>(Arrays.asList("admin", "user", "demo")));
        }

        return INVALID_RESULT;
    }
}
