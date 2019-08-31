package io.hustler.wallzy.model.wallzy.response;


import io.hustler.wallzy.model.base.BaseResponse;

public class ResSignupDealer extends BaseResponse {
    private String name, mobileNumber, systemAuthToken, fbAuthToken;
    private Long id;
    private Double balanceLoad;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSystemAuthToken() {
        return systemAuthToken;
    }

    public void setSystemAuthToken(String systemAuthToken) {
        this.systemAuthToken = systemAuthToken;
    }

    public String getFbAuthToken() {
        return fbAuthToken;
    }

    public void setFbAuthToken(String fbAuthToken) {
        this.fbAuthToken = fbAuthToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBalanceLoad() {
        return balanceLoad;
    }

    public void setBalanceLoad(Double balanceLoad) {
        this.balanceLoad = balanceLoad;
    }
}
