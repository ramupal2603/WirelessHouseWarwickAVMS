package com.adverticoLTD.avms.data.normalContractor;

import com.adverticoLTD.avms.data.normalVisitor.NormalVisitorResponseDataModel;

public class NormalContractorResponseModel {

    String status;
    NormalContractorResponseDataModel data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public NormalContractorResponseDataModel getData() {
        return data;
    }

    public void setData(NormalContractorResponseDataModel data) {
        this.data = data;
    }
}
