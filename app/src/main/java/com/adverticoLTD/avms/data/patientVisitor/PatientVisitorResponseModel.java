package com.adverticoLTD.avms.data.patientVisitor;

public class PatientVisitorResponseModel {
    String status;
    PatientVisitorResponseDataModel data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PatientVisitorResponseDataModel getData() {
        return data;
    }

    public void setData(PatientVisitorResponseDataModel data) {
        this.data = data;
    }
}
