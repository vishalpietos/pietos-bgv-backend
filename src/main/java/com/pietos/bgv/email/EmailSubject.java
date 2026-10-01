package com.pietos.bgv.email;

public final class EmailSubject {

    private EmailSubject() {
    }

    // ==============================
    // Account Creation
    // ==============================

    public static final String CLIENT_CREATED =
            "Welcome to Pietos BGV";

    public static final String MANAGE_USER_CREATED =
            "Welcome to Pietos BGV";

    public static final String SYSTEM_USER_CREATED =
            "Welcome to Pietos BGV";
    
    public static final String MANAGE_CLIENT_ADMIN_CREATED =
            "Manage Client Admin Account Created";

    public static final String MANAGE_HR_USER_CREATED =
            "Manage HR User Account Created";

    public static final String MANAGE_HR_USER_CONFIRMATION =
            "New HR User Created";
    
    public static final String MANAGE_USER_CREATED_CONFIRMATION =
            "New Manage User Created";
    
    public static final String INTERNAL_USER_CREATED_CONFIRMATION =
            "Internal User Created - BGV Portal";

    // ==============================
    // Account Updates
    // ==============================

    public static final String EMAIL_UPDATED =
            "Email Updated Successfully";

    public static final String PASSWORD_CHANGED =
            "Password Changed Successfully";

    public static final String PASSWORD_RESET =
            "Password Reset";

    public static final String ACCOUNT_ACTIVATED =
            "Account Activated";

    public static final String ACCOUNT_DEACTIVATED =
            "Account Deactivated";

    // ==============================
    // Future BGV Notifications
    // ==============================

    public static final String CASE_ASSIGNED =
            "New Verification Assigned";

    public static final String CASE_COMPLETED =
            "Verification Completed";

}