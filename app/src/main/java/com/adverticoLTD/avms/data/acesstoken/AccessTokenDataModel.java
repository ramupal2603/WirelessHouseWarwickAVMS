package com.adverticoLTD.avms.data.acesstoken;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccessTokenDataModel {


    @SerializedName("api_token_key")
    @Expose
    private String apiTokenKey;

    public String getApiTokenKey() {
        return apiTokenKey;
    }

    public void setApiTokenKey(String apiTokenKey) {
        this.apiTokenKey = apiTokenKey;
    }

}
