package com.christer.project;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-02 15:00
 * Description:
 * Web URL 统一管理
 */
public final class WebURLConstant {

    private WebURLConstant() {
    }

    private static final String URI_USER = "/user";
    private static final String URI_PAGE = "/page";

    public static final String URI_USER_REGISTER = URI_USER +  "/register";

    public static final String URI_USER_LOGIN = URI_USER + "/login";

    public static final String URI_USER_LOGOUT = URI_USER + "/logout";

    public static final String URI_USER_INFO = URI_USER + "/info";
    public static final String URI_USER_PAGE = URI_USER + URI_PAGE;

    public static final String URI_POST = "/post";

    public static final String URI_POST_PAGE = URI_POST + URI_PAGE;
}
