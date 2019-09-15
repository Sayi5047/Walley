package io.hustler.wallzy.model.wallzy.response;


import java.io.Serializable;

import io.hustler.wallzy.model.base.BaseResponseUser;

public class ResLoginUser extends BaseResponseUser implements Serializable {
    private String fbAuthToken;
    private String sysAuthToken;
    private String personId;

    public String getFbAuthToken() {
        return fbAuthToken;
    }

    public void setFbAuthToken(String fbAuthToken) {
        this.fbAuthToken = fbAuthToken;
    }

    public String getSysAuthToken() {
        return sysAuthToken;
    }

    public void setSysAuthToken(String sysAuthToken) {
        this.sysAuthToken = sysAuthToken;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
