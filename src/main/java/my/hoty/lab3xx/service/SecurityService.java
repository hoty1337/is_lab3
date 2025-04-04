package my.hoty.lab3xx.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface SecurityService {
    void autoLogin(String username, String password, HttpServletRequest request, HttpServletResponse response);
}
