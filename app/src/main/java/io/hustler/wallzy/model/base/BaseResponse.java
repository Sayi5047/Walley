package io.hustler.wallzy.model.base;

public class BaseResponse {
    private boolean apiSuccess;
    private String message;
    private int statuscode;
    private boolean newTokenGenerated;
    private String newToken;

    public boolean isNewTokenGenerated() {
        return newTokenGenerated;
    }

    public void setNewTokenGenerated(boolean newTokenGenerated) {
        this.newTokenGenerated = newTokenGenerated;
    }

    public String getNewToken() {
        return newToken;
    }

    public void setNewToken(String newToken) {
        this.newToken = newToken;
    }

    public boolean isApiSuccess() {
        return apiSuccess;
    }

    public void setApiSuccess(boolean apiSuccess) {
        this.apiSuccess = apiSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }
}
