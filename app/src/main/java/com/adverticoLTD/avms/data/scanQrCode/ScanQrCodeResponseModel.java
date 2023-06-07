package com.adverticoLTD.avms.data.scanQrCode;

import java.util.ArrayList;

public class ScanQrCodeResponseModel {
    String status;
    ScanQrCodeResponseDataModel data;

    public ScanQrCodeResponseDataModel getData() {
        return data;
    }

    public void setData(ScanQrCodeResponseDataModel data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
