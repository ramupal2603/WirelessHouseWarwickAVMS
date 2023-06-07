package com.adverticoLTD.avms.data.normalVisitor;

public class NormalVisitorResponseModel {

    String status;
    NormalVisitorResponseDataModel data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public NormalVisitorResponseDataModel getData() {
        return data;
    }

    public void setData(NormalVisitorResponseDataModel data) {
        this.data = data;
    }
}
