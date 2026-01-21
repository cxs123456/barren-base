package org.barren.core.auth.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author ahuang@fintechautomation.com
 * @since 2021-07-24 5:20 AM
 **/
public class UserContextHolder {

    private static final String USER_CONTEXT_ATTRIBUTES = "USER_TOKEN_CONTEXT";

    // public static void setContext(TokenAuthentication context) {
    //     RequestContextHolder.currentRequestAttributes().setAttribute(
    //             USER_CONTEXT_ATTRIBUTES,
    //             context,
    //             RequestAttributes.SCOPE_REQUEST);
    // }
    //
    // public static TokenAuthentication get() {
    //     return (TokenAuthentication) RequestContextHolder.currentRequestAttributes()
    //             .getAttribute(USER_CONTEXT_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);
    // }
}
