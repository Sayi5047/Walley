package io.hustler.wallzy.model.wallzy.request;

public class ReqUpdateFcmToken {
    private
    String fmcToken;
    private long userId;

    public String getFmcToken() {
        return fmcToken;
    }

    public void setFmcToken(String fmcToken) {
        this.fmcToken = fmcToken;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
