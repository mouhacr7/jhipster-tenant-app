package com.tenant.app.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$|^[a-z][a-z0-9]*$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "fr";
    public static final String DEFAULT_PASSWORD = "123546";
    public static final String ROLE_EMPLOYEE = "ROLE_EMPLOYEE";

    private Constants() {}
}
