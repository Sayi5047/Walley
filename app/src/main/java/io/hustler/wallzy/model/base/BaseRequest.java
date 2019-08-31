package io.hustler.wallzy.model.base;

public class BaseRequest {
    private String origin="MOBILE_ANDROID";
    private String version="1.0";
    private String country;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
