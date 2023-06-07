package com.adverticoLTD.avms.keyLogSolution.data.signIn;

public class KeySignInRequestParamModel {

    String key_reference_id;
    String staff_id;
    String signature1;
    String signature2;

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
