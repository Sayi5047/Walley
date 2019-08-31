package io.hustler.wallzy.model.wallzy.request;

import io.hustler.wallzy.model.base.BaseRequest;

public class ReqEmailLogin extends BaseRequest {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
