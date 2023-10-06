package com.adverticoLTD.avms.keyLogSolution.data.signOut;

public class KeySignOutRequestParamModel {

    String key_reference_id;
    String staff_id;
    String first_name;
    String sur_name;
    String company_name;
    String mobile_no;
    String signature1;
    String signature2;
    String signout_duration;

    public String getSignout_duration() {
        return signout_duration;
    }

    public void setSignout_duration(String signout_duration) {
        this.signout_duration = signout_duration;
    }



    public String getKey_reference_id() {
        return key_reference_id;
    }

    public void setKey_reference_id(String key_reference_id) {
        this.key_reference_id = key_reference_id;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSur_name() {
        return sur_name;
    }

    public void setSur_name(String sur_name) {
        this.sur_name = sur_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getSignature1() {
        return signature1;
    }

    public void setSignature1(String signature1) {
        this.signature1 = signature1;
    }

    public String getSignature2() {
        return signature2;
    }

    public void setSignature2(String signature2) {
        this.signature2 = signature2;
    }
}
