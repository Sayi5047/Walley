package io.hustler.wallzy.model.wallzy.request;

import io.hustler.wallzy.model.base.BaseRequest;

public class ReqGoogleSignup extends BaseRequest {
    private String googleIdToken;

    public String getGoogleIdToken() {
        return googleIdToken;
    }

    public void setGoogleIdToken(String googleIdToken) {
        this.googleIdToken = googleIdToken;
    }
}
