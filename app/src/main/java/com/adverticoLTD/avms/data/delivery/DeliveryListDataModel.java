package com.adverticoLTD.avms.data.delivery;

public class DeliveryListDataModel {

    String id;
    String courier_name;
    String company_id;
    String staff_id;
    String created_datetime;
    String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourier_name() {
        return courier_name;
    }

    public void setCourier_name(String courier_name) {
        this.courier_name = courier_name;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(String created_datetime) {
        this.created_datetime = created_datetime;
    }
}
