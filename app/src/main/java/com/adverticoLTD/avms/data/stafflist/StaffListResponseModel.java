package com.adverticoLTD.avms.data.stafflist;

import java.util.ArrayList;

public class StaffListResponseModel {

    String status;
    ArrayList<StaffListResponseDataModel> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<StaffListResponseDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<StaffListResponseDataModel> data) {
        this.data = data;
    }
}
