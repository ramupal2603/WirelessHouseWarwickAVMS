package com.adverticoLTD.avms.keyLogSolution.data.keyRefList;

import com.google.gson.annotations.SerializedName;

public class KeyResponseDataModel {

    String id;
    @SerializedName("key_name")
    String name;
    String key_no;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey_no() {
        return key_no;
    }

    public void setKey_no(String key_no) {
        this.key_no = key_no;
    }
}
