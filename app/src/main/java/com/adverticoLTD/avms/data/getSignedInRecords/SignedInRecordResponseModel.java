package com.adverticoLTD.avms.data.getSignedInRecords;

import java.util.List;

public class SignedInRecordResponseModel {

    String status;
    List<SignedInRecordsDataModel> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SignedInRecordsDataModel> getData() {
        return data;
    }

    public void setData(List<SignedInRecordsDataModel> data) {
        this.data = data;
    }
}

