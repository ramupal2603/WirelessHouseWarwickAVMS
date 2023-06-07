package com.adverticoLTD.avms.keyLogSolution.data.staffList;

import java.util.ArrayList;

public class StaffListResponseModel {

    String status;
    ArrayList<StaffListDataModel> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<StaffListDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<StaffListDataModel> data) {
        this.data = data;
    }
}
