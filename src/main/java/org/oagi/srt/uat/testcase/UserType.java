package org.oagi.srt.uat.testcase;

public enum UserType {

    OAGI("OAGI Tenant"),
    Enterprise("Enterprise Tenant"),
    Free("Free Tenant"),
    Root("Root Tenant");

    private String str;

    UserType(String str) {
        this.str = str;
    }

    public String toString() {
        return this.str;
    }

}
