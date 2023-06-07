package com.adverticoLTD.avms.data.acesstoken;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccessTokenResponseModel {


    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private AccessTokenDataModel data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public AccessTokenDataModel getData() {
        return data;
    }

    public void setData(AccessTokenDataModel data) {
        this.data = data;
    }


}
