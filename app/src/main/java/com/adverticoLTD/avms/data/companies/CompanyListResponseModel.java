package com.adverticoLTD.avms.data.companies;

import java.util.ArrayList;

public class CompanyListResponseModel {

    String status;
    ArrayList<CompanyListDataModel> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<CompanyListDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<CompanyListDataModel> data) {
        this.data = data;
    }
}
