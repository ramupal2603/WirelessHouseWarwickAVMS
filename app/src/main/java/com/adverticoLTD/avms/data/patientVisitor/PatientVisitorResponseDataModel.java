package com.adverticoLTD.avms.data.patientVisitor;

public class PatientVisitorResponseDataModel {

    String log_id;
    String visitor_id;
    String name;
    String vehicle_registration;
    String here_to_visit;
    String status;

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getVisitor_id() {
        return visitor_id;
    }

    public void setVisitor_id(String visitor_id) {
        this.visitor_id = visitor_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVehicle_registration() {
        return vehicle_registration;
    }

    public void setVehicle_registration(String vehicle_registration) {
        this.vehicle_registration = vehicle_registration;
    }

    public String getHere_to_visit() {
        return here_to_visit;
    }

    public void setHere_to_visit(String here_to_visit) {
        this.here_to_visit = here_to_visit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
