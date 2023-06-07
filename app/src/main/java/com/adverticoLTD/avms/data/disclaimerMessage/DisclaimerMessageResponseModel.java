package com.adverticoLTD.avms.data.disclaimerMessage;

import java.util.ArrayList;

public class DisclaimerMessageResponseModel {

    String status;
    ArrayList<DisclaimerMessageResponseDataModel> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<DisclaimerMessageResponseDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<DisclaimerMessageResponseDataModel> data) {
        this.data = data;
    }
}
