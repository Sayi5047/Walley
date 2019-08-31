package io.hustler.wallzy.model.wallzy.response;


import io.hustler.wallzy.model.base.BaseResponse;

import java.io.Serializable;

public class ResDealerLogin extends BaseResponse implements Serializable {

    private Data data;
    public Dealer dealer;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public static class Data {
        private String user, pass, basic;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

        public String getBasic() {
            return basic;
        }

        public void setBasic(String basic) {
            this.basic = basic;
        }
    }

    public static class Dealer {
        private Long dealerId;
        private String dealerName;
        private String mobileNumber;
        private Double loadBalance;
        private boolean isFirstLogin;

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }


        public Long getId() {
            return dealerId;
        }

        public void setId(Long id) {
            this.dealerId = id;
        }

        public Double getLoadBalance() {
            return loadBalance;
        }

        public void setLoadBalance(Double loadBalance) {
            this.loadBalance = loadBalance;
        }

        public String getDealerName() {
            return dealerName;
        }

        public void setDealerName(String dealerName) {
            this.dealerName = dealerName;
        }

        public Long getDealerId() {
            return dealerId;
        }

        public void setDealerId(Long dealerId) {
            this.dealerId = dealerId;
        }

        public boolean isFirstLogin() {
            return isFirstLogin;
        }

        public void setFirstLogin(boolean firstLogin) {
            isFirstLogin = firstLogin;
        }

    }
}
