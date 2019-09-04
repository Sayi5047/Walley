package io.hustler.wallzy.model.wallzy.response;


import io.hustler.wallzy.model.base.BaseResponse;

public class ResGetCatCollCount extends BaseResponse {
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
