package com.adverticoLTD.avms.data.existingContractor;

public class ExistingContractorResponseModel {
    String status;
    ExistingContractorResponseDataModel data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ExistingContractorResponseDataModel getData() {
        return data;
    }

    public void setData(ExistingContractorResponseDataModel data) {
        this.data = data;
    }
}
