package com.csStudy.CardGame.security;

import javax.servlet.http.HttpServletRequest;

public interface SecurityUtil {
    String getIp(HttpServletRequest request);
}
