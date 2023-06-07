package com.adverticoLTD.avms.keyLogSolution.data.keyRefList;

import java.util.ArrayList;

public class KeyResponseModel {

    String status;
    ArrayList<KeyResponseDataModel> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<KeyResponseDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<KeyResponseDataModel> data) {
        this.data = data;
    }
}
