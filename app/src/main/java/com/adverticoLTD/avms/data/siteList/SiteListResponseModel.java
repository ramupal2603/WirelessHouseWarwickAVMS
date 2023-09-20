package com.adverticoLTD.avms.data.siteList;

import java.util.ArrayList;

public class SiteListResponseModel {

    String status;
    ArrayList<SiteListResponseDataModel> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<SiteListResponseDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<SiteListResponseDataModel> data) {
        this.data = data;
    }
}
