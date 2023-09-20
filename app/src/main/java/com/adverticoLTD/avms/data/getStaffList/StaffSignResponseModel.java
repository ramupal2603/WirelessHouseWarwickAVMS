package com.adverticoLTD.avms.data.getStaffList;



import com.adverticoLTD.avms.data.nameList.NameListDataModel;

import java.util.ArrayList;

public class StaffSignResponseModel {
    String status;
    ArrayList<NameListDataModel> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<NameListDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<NameListDataModel> data) {
        this.data = data;
    }
}
