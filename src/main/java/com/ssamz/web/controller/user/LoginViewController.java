package com.ssamz.web.controller.user;

import com.ssamz.web.controller.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 로그인 화면 요청 (/loginView.do)을 처리할 컨트롤러
public class LoginViewController implements Controller {
    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("로그인 화면으로 이동");
        return "login"; //화면이동
    }
}
