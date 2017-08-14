package org.oagi.srt.uat.testcase;

public enum UserRole {

    // for OAGI Tenant
    Developer("Developer"),
    AdminDeveloper("Admin Developer"),

    // for Enterprise Tenant
    EndUser("End User"),
    AdminUser("Admin User"),

    // for Free Tenant
    Free("Free"),

    // for Root Tenant
    Root("Root");

    private String str;

    UserRole(String str) {
        this.str = str;
    }

    public String toString() {
        return this.str;
    }

}
