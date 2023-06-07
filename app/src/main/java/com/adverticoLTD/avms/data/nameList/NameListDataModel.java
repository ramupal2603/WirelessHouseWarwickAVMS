package com.adverticoLTD.avms.data.nameList;

public class NameListDataModel {
    String id;
    String name;
    String user_type;
    String visitor_organization;

    public String getVisitor_organization() {
        return visitor_organization;
    }

    public void setVisitor_organization(String visitor_organization) {
        this.visitor_organization = visitor_organization;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
