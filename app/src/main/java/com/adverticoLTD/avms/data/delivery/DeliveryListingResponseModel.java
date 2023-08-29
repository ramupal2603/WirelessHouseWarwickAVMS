package com.adverticoLTD.avms.data.delivery;

import com.adverticoLTD.avms.data.companies.CompanyListDataModel;

import java.util.ArrayList;

public class DeliveryListingResponseModel {

    String status;
    ArrayList<DeliveryListDataModel> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<DeliveryListDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<DeliveryListDataModel> data) {
        this.data = data;
    }
}
