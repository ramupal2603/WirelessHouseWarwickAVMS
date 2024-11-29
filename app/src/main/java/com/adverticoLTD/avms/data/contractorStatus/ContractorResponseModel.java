package com.adverticoLTD.avms.data.contractorStatus;

public class ContractorResponseModel {
    String status;
    String contractor_name;
    ContractorResponseDataModel data;

    public String getContractor_name() {
        return contractor_name;
    }

    public void setContractor_name(String contractor_name) {
        this.contractor_name = contractor_name;
    }

    public ContractorResponseDataModel getData() {
        return data;
    }

    public void setData(ContractorResponseDataModel data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
